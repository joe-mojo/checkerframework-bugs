package org.jro.tests.checkerfwk.utils;


import io.vavr.PartialFunction;
import io.vavr.control.Either;
import org.checkerframework.checker.nullness.qual.NonNull;

public class Utils {
	private Utils() {
	}

	public static @NonNull <L, R> PartialFunction<Either<L, R>, R> createKeepRight() {
		return new PartialFunction<>() {

			@Override
			@NonNull public R apply(final @NonNull Either<L, R> either) {
				return either.get();
			}

			@Override
			public boolean isDefinedAt(final @NonNull Either<L, R> value) {
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
			public boolean isDefinedAt(final @NonNull Either<L, ? extends R> value) {
				return value.isRight();
			}
		};
	}
}
