package com.parousia.fuellogger.views.fragments.components;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.View;

import com.parousia.fuellogger.db.FuelDataSource;
import com.parousia.fuellogger.model.FuelEntry;

public class GraphView extends View {

	private Bitmap mBitmap;
	private Canvas mCanvas;

	private Paint mBitmapPaint;
	int averageGraphColor = Color.GREEN;
	private Paint graphPaint;
	private FuelDataSource dataSource;
	private Context context;
	private List<FuelEntry> dataEntries;

	public GraphView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public GraphView(Context context) {
		super(context);
		this.context = context;
		init();
	}

	public GraphView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onDraw(Canvas canvas) {

		drawGraphOnCanvas(canvas);
	}

	protected void init() {

		dataSource = new FuelDataSource(context);
		dataSource.open();

		mBitmapPaint = new Paint(Paint.DITHER_FLAG);

		graphPaint = new Paint();
		graphPaint.setColor(Color.WHITE);
		graphPaint.setStyle(Paint.Style.FILL_AND_STROKE);
		graphPaint.setStrokeJoin(Paint.Join.ROUND);
		graphPaint.setStrokeCap(Paint.Cap.BUTT);
		graphPaint.setStrokeWidth(4);
		graphPaint.setAntiAlias(true);

	}

	private void resetBitmap(Canvas canvas) {
		mBitmap = Bitmap.createBitmap(canvas.getWidth(), canvas.getHeight(),
				Bitmap.Config.ARGB_8888);
		mCanvas = new Canvas(mBitmap);
		canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
	}

	protected void drawGraphOnCanvas(Canvas canvas) {
		if (mBitmap == null) {
			resetBitmap(canvas);
		}
	}

	private void calculatePaths() {
		dataEntries = dataSource.getAllEntries();

		AsyncTask<Object, Integer, Object> task = new AsyncTask<Object, Integer, Object>() {

			final List<Path> pathList = new ArrayList<Path>();

			@Override
			protected Object doInBackground(Object... params) {
				Point pPoint = new Point(), cpoint = new Point();
				cpoint.x = cpoint.y = pPoint.x = pPoint.y = 0f;
				for (FuelEntry entry : dataEntries) {
					double price = entry.getFuelPrice();
					long odo = entry.getOdometer();
					Path path = new Path();
					path.moveTo(pPoint.x, pPoint.y);
					path.quadTo((cpoint.x + pPoint.x) / 2,
							(cpoint.y + pPoint.y) / 2, cpoint.x, cpoint.y);
					pathList.add(path);
				}
				return null;
			}
			@Override
			protected void onPostExecute(Object result) {
				updateGraph();
			}

		};
		task.execute("");

	}
	
	public void drawGraph(){
		calculatePaths();
		updateGraph();
	}

	protected void updateGraph() {
		invalidate();
	}

	class Point {
		float x, y;

		@Override
		public String toString() {
			return x + ", " + y;
		}
	}
}
