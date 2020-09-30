package orj.jro.tests.checkerfwk.utils;

import java.io.IOException;

@FunctionalInterface
public interface UnsafeIoSupplier<R> {
	R get() throws IOException;
}
