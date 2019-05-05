package org.drama.core;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Enumeration;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.jar.JarFile;

import static org.drama.delegate.Delegator.action;

class DramaClassloader extends ClassLoader {
    private static final String CLASS_FILE_ENDWITH = ".class";
    private static final String WILDCARD = "*";
    private static final String PACKAGE_SPLITER = "\\.";

    DramaClassloader() {
        super();
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        return super.findClass(name);
    }

    void scan(final String packageName, Consumer<Class<?>> onFound) {
        if(StringUtils.isBlank(packageName)) {
            return;
        }

        String[] parts = packageName.split(PACKAGE_SPLITER);

        if(ArrayUtils.isEmpty(parts) || WILDCARD.equals(parts[0])) {
            return;
        }

        StringBuilder buffer = new StringBuilder();

        for(int i=0, j=parts.length; i<j; i++) {
            if(WILDCARD.equals(parts[i])) {
                break;
            }

            buffer.append(String.format("%s%s", i != 0 ? File.separator : "", parts[i]));
        }

        String packagePath = buffer.toString();

        try {
            Enumeration<URL> urls = getResources(packagePath);

            while(urls.hasMoreElements()) {
                URL url = urls.nextElement();

                switch (url.getProtocol().toLowerCase()) {
                    case "file":
                        scanClassFile(packageName, url, onFound);
                        break;
                    case "jar":
                        String newPath = formatJarFilePath(url);

                        scanJarFile(packagePath, newPath, onFound);
                        break;
                    default:
                        return;
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String formatJarFilePath(URL url) {
        String path = url.getPath();

        int startIndex = path.indexOf(":") + 1;
        int endIndex = path.lastIndexOf("!");

        return path.substring(startIndex, endIndex);
    }

    private void scanJarFile(final String packagePath, String url, Consumer<Class<?>> onFound) throws IOException {
        final JarFile jarFile = new JarFile(url);

        jarFile.stream().filter(e -> {
            String name = e.getName();
            return name.indexOf(packagePath) == 0 && name.lastIndexOf(CLASS_FILE_ENDWITH) > 1;
        }).forEach(e -> {
            try(InputStream inputStream = jarFile.getInputStream(e)) {
                int capacity = (int)e.getSize();

                if(capacity == 0) {
                    return;
                }

                byte[] bytes = new byte[capacity];
                int nread = 0;
                int n;
                while ((n = inputStream.read(bytes, nread, capacity - nread)) > 0) {
                    nread += n;
                }

                if(n < 0) {
                    return;
                }

                String className = e.getName().replaceAll(CLASS_FILE_ENDWITH, "").replaceAll("/", PACKAGE_SPLITER);
                Class<?> c = defineClass(className, bytes, 0, bytes.length);

                if(Objects.nonNull(c)) {
                    action(onFound, c);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }

    private void scanClassFile(final String packageName, final URL url, Consumer<Class<?>> onFound) {
        scanFile(url.getPath(), p -> {
            String fileName = p.getFileName().toString();

            int endIndex;

            if((endIndex = fileName.lastIndexOf(CLASS_FILE_ENDWITH)) < 1) {
                return;
            }

            try {
                String className = String.format("%s.%s", packageName, fileName.substring(0, endIndex));

                byte[] bytes = Files.readAllBytes(p);

                Class<?> c = defineClass(className, bytes, 0, bytes.length);

                if(Objects.nonNull(c)) {
                    action(onFound, c);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void scanFile(final String filePath, Consumer<Path> onFile) {
        Path path = FileSystems.getDefault().getPath(filePath);

        try {
            if(!Files.isDirectory(path)) {
                action(onFile, path);
                return;
            }

            Files.list(path).forEach(p -> {
                if(!Files.isDirectory(p)) {
                    action(onFile, p);
                } else {
                    scanFile(p.toString(), onFile);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
