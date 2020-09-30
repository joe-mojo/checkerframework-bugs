package orj.jro.tests.checkerfwk.utils;


import io.vavr.Function0;
import io.vavr.Function1;
import io.vavr.PartialFunction;
import io.vavr.control.Either;
import org.checkerframework.checker.nullness.qual.NonNull;
import orj.jro.tests.checkerfwk.errors.DecoderError;

import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;

public class Utils {
	private Utils() {
	}

	public static boolean isFatal(final Throwable err) {
		return (err instanceof VirtualMachineError)
				|| (err instanceof ThreadDeath)
				|| (err instanceof InterruptedException)
				|| (err instanceof LinkageError);
	}

	public static <L, R> @NonNull Either<L, R> tryNonFatal(final Function0<R> code, final Function1<Throwable, L> toLeft) {
		try {
			return Either.right(code.apply());
		} catch (Throwable err) {
			if (isFatal(err)) throw err;
			return Either.left(toLeft.apply(err));
		}
	}

	public static @NonNull Either<DecoderError.IO, String> loadUTF8(final @NonNull String filePath) {
		return DecoderError.tryIO(
				() -> new FileInputStream(filePath),
				(is) -> new String(is.readAllBytes(), StandardCharsets.UTF_8)
		);
	}

	public static @NonNull <L, R> Either<L, R> flattenLeft(Either<L, Either<L, R>> ee) {
		return ee.flatMap(Function1.identity());
	}


	public static @NonNull <L, R> PartialFunction<Either<L, R>, R> createKeepRight() {
		return new PartialFunction<>() {

			@Override
			@NonNull public R apply(final @NonNull Either<L, R> either) {
				return either.get();
			}

			@Override
			@NonNull public boolean isDefinedAt(final @NonNull Either<L, R> value) {
				return value.isRight();
			}
		};
	}

	public static @NonNull <L, R> PartialFunction<Either<L, ? extends R>, ? extends R> createRCovariantKeepRight() {
		return new PartialFunction<>() {

			@Override
			@NonNull public R apply(final @NonNull Either<L, ? extends R> either) {
				return either.get();
			}

			@Override
			@NonNull public boolean isDefinedAt(final @NonNull Either<L, ? extends R> value) {
				return value.isRight();
			}
		};
	}
}
