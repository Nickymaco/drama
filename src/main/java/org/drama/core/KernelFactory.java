package org.drama.core;

import org.drama.security.Signature;

final class KernelFactory {
    private static KernelFactory ourInstance = new KernelFactory();

    public static KernelFactory getInstance() {
        return ourInstance;
    }

    private KernelFactory() {
    }

    public Kernel getKernel(Signature signature) {
        return DramaKernel.getInstance(signature);
    }
}
