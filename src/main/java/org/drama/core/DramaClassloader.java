package org.drama.core;

import static org.drama.delegate.Delegator.action;
import static org.drama.delegate.Delegator.forEach;
import static org.drama.text.MessageText.format;
import static org.drama.text.Symbol.WILDCARD;
import static org.drama.text.Symbol.EMPTY;

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

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

class DramaClassloader extends ClassLoader {
    private static final String CLASS_FILE_ENDWITH = ".class";
    private static final String PACKAGE_SPLITER = "\\.";

    DramaClassloader() {
        super();
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        return super.findClass(name);
    }

    void scan(final String packageName, Consumer<Class<?>> onFound) {
        if (StringUtils.isBlank(packageName)) {
            return;
        }

        String[] parts = packageName.split(PACKAGE_SPLITER);

        if (ArrayUtils.isEmpty(parts) || WILDCARD.equals(parts[0])) {
            return;
        }

        StringBuilder buffer = new StringBuilder();

        forEach(parts, (part, i) -> {
            if (WILDCARD.equals(part)) {
                return true;
            }

            buffer.append(format("{0}{1}", i != 0 ? File.separator : EMPTY, part));
            return false;
        });

        String packagePath = buffer.toString();

        try {
            Enumeration<URL> urls = getResources(packagePath);

            while (urls.hasMoreElements()) {
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
        try(final JarFile jarFile = new JarFile(url)) {
	        jarFile.stream().filter(e -> {
	            String name = e.getName();
	            return name.indexOf(packagePath) == 0 && name.lastIndexOf(CLASS_FILE_ENDWITH) > 1;
	        }).forEach(e -> {
	            try (InputStream inputStream = jarFile.getInputStream(e)) {
	                int capacity = (int) e.getSize();
	
	                if (capacity == 0) {
	                    return;
	                }
	
	                byte[] bytes = new byte[capacity];
	                int nread = 0;
	                int n;
	                while ((n = inputStream.read(bytes, nread, capacity - nread)) > 0) {
	                    nread += n;
	                }
	
	                if (n < 0) {
	                    return;
	                }
	
	                String className = e.getName().replaceAll(CLASS_FILE_ENDWITH, EMPTY).replaceAll("/", PACKAGE_SPLITER);
	                Class<?> c = defineClass(className, bytes, 0, bytes.length);
	
	                if (Objects.nonNull(c)) {
	                    action(onFound, c);
	                }
	            } catch (IOException ex) {
	                ex.printStackTrace();
	            }
	        });
        } catch(Exception ex) {
        	ex.printStackTrace();
        }
    }

    private void scanClassFile(final String packageName, final URL url, Consumer<Class<?>> onFound) {
        scanFile(url.getPath(), p -> {
            String fileName = p.getFileName().toString();

            int endIndex;

            if ((endIndex = fileName.lastIndexOf(CLASS_FILE_ENDWITH)) < 1) {
                return;
            }

            try {
                String className = format("{0}.{1}", packageName, fileName.substring(0, endIndex));

                byte[] bytes = Files.readAllBytes(p);

                Class<?> c = defineClass(className, bytes, 0, bytes.length);

                if (Objects.nonNull(c)) {
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
            if (!Files.isDirectory(path)) {
                action(onFile, path);
                return;
            }

            Files.list(path).forEach(p -> {
                if (!Files.isDirectory(p)) {
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
