package com.example.polisdemo.collage.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Path;

public class Utils {

	public static int generatRandomPositiveNegitiveValue(int max, int min) {
		int ii = -min + (int) (Math.random() * ((max - (-min)) + 1));
		return ii;
	}

	public static Bitmap generateCircularBitmap(Bitmap input) {

		final int width = input.getWidth();
		final int height = input.getHeight();
		final Bitmap outputBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

		final Path path = new Path();
		path.addCircle(
				(float) (width / 2)
				, (float) (height / 2)
				, (float) Math.min(width, (height / 2))
				, Path.Direction.CCW
		);

		final Canvas canvas = new Canvas(outputBitmap);
		canvas.clipPath(path);
		canvas.drawBitmap(input, 0, 0, null);
		return outputBitmap;
	}

}
