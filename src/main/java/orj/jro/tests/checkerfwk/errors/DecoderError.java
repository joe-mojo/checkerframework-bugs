package orj.jro.tests.checkerfwk.errors;

import io.vavr.control.Either;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import orj.jro.tests.checkerfwk.utils.UnsafeIoFunction;
import orj.jro.tests.checkerfwk.utils.UnsafeIoSupplier;

import java.io.Closeable;
import java.io.IOException;
import java.util.Objects;

import static io.vavr.API.Left;
import static io.vavr.API.Right;

public abstract class DecoderError {
	private DecoderError(){}

	public static DecoderError.IO io(final IOException cause){
		return new DecoderError.IO(cause);
	}

	public static <R> Either<IO, R> tryIO(final @NonNull UnsafeIoSupplier<R> unsafeCode) {
		try {
			return Right(unsafeCode.get());
		} catch (IOException ex) {
			return Left(io(ex));
		}
	}

	public static <Resource extends Closeable, Result> Either<DecoderError.IO, Result> tryIO(
			final @NonNull UnsafeIoSupplier<Resource> createCloseable,
			final @NonNull UnsafeIoFunction<Resource, Result> unsafeCode
	) {
		try(final var resource = createCloseable.get()) {
			return Right(unsafeCode.apply(resource));
		} catch (IOException ex) {
			return Left(io(ex));
		}
	}

	public static final class IO extends RuntimeError {
		public IO(final IOException cause) {
			super(cause);
		}
	}

	public static abstract class RuntimeError  extends DecoderError {
		private final @NonNull Throwable cause;

		protected RuntimeError(final @NonNull Throwable cause){
			this.cause = cause;
		}

		public @NonNull Throwable cause(){
			return this.cause;
		}

		@Override
		public boolean equals(@Nullable Object o) {
			if (this == o) return true;
			if (!(o instanceof RuntimeError)) return false;
			RuntimeError that = (RuntimeError) o;
			return cause.equals(that.cause);
		}

		@Override
		public int hashCode() {
			return Objects.hash(cause);
		}
	}
}
