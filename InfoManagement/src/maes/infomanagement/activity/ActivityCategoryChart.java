package maes.infomanagement.activity;


import java.util.List;

import maes.infomanagement.activity.base.ActivityFrame;
import maes.infomanagement.model.ModelCategoryTotal;

import org.achartengine.ChartFactory;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ActivityCategoryChart extends ActivityFrame {
	private List<ModelCategoryTotal> mModelCategoryTotal;
	private TextView tvContent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initVariable();
		View pieView = CategoryStatistics();
		appendMainBody(pieView);
		removeBottomBox();
	}

	/**
	 * 
	 * @return
	 */
	private View CategoryStatistics() {
		int[] colors = new int[] { Color.parseColor("#FF5552"), Color.parseColor("#2A94F1"),
				Color.parseColor("#F12792"), Color.parseColor("#FFFF52"), Color.parseColor("#84D911"),
				Color.parseColor("#5255FF") };
		DefaultRenderer defaultRenderer = buildCategoryRenderer(colors);
		CategorySeries categorySeries = buildCategoryDataset("消费类别统计", mModelCategoryTotal);
		View pieView = ChartFactory.getPieChartView(this, categorySeries, defaultRenderer);
		return pieView;
	}
	protected DefaultRenderer buildCategoryRenderer(int[] colors) {
        DefaultRenderer _Renderer = new DefaultRenderer();
        _Renderer.setZoomButtonsVisible(true);
        _Renderer.setLabelsTextSize(15);
        _Renderer.setLegendTextSize(15);
        _Renderer.setLabelsColor(Color.BLUE);
        _Renderer.setMargins(new int[] { 20, 30, 15, 10 });
        int _Color = 0;
        for (int i = 0;i<mModelCategoryTotal.size();i++) {
          SimpleSeriesRenderer _R = new SimpleSeriesRenderer();
          _R.setColor(colors[_Color]);
          _Renderer.addSeriesRenderer(_R);
          _Color++;
          if (_Color > colors.length) {
        	  _Color = 0;
          }
        }
        return _Renderer;
      }
	protected CategorySeries buildCategoryDataset(String title, List<ModelCategoryTotal> values) {
		CategorySeries series = new CategorySeries(title);
		for (ModelCategoryTotal value : values) {
			series.add(value.CategoryName+"数量： " + value.Count, Double.parseDouble(value.Count));
		}

		return series;
	}

	private void initVariable() {
		mModelCategoryTotal = (List<ModelCategoryTotal>) getIntent().getSerializableExtra("Total");

	}
}
