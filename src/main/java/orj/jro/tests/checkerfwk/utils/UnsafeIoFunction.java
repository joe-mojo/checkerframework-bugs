package orj.jro.tests.checkerfwk.utils;


import java.io.Closeable;
import java.io.IOException;

@FunctionalInterface
public interface UnsafeIoFunction<Resource extends Closeable, Result>{
	Result apply(Resource ioResource) throws IOException;
}
