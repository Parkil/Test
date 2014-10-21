package test.jfreechart;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.io.File;

import org.jfree.chart.ChartFrame;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.CategoryItemLabelGenerator;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.DatasetRenderingOrder;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.TextAnchor;


/*
 * JFreeChart api를 이용하여 Bar Chart를 그리는 예제
 * 
 * 사용 API
 * jcommon-1.0.20.jar
 * jfreechart-1.0.16.jar
 */
public class BarChartTest {

	// Run As > Java Application 으로 실행하면 바로 확인할 수 있음.
	public static void main(final String[] args) throws Exception{
		BarChartTest demo = new BarChartTest();
		//demo.ViewChartAsFrame(); //JFrame 기반으로 표시
		demo.ViewChartAsPng("d:/chart.png"); //png파일 기반으로 저장
	}
	
	
	public void ViewChartAsFrame() {
		JFreeChart chart = getChart();
		ChartFrame frame1 = new ChartFrame("Bar Chart", chart);
		frame1.setSize(800, 400);
		frame1.setVisible(true);
	}
	
	public void ViewChartAsPng(String path) throws Exception{
		JFreeChart chart = getChart();
		ChartUtilities.saveChartAsPNG(new File(path), chart, 1200, 600);
	}
	
	/*
	 * ServletOutputStream 을 이용하여 이미지를 표시하는 예제
	 * ChartUtilities.saveChartAsPNG(new File(path), chart, 800, 300);를 이용하여 파일을 저장하고 
	 * <img src=""/>에 파일로 저장된 이미지 경로를 표시하여 이미지를 표시하게 되면 기존 이미지가 웹브라우저 캐시에 남아 있어 
	 * 실제 이미지파일은 변경되었는데도 브라우저 화면에는 기존 이미지 파일이 표시되는 문제가 생긴다(특히 신/구 파일명이 같은경우) 그런경우를 방지하기 위하여  
	 * ServletOutputStream으로 이미지파일을 바로 전송한다.
	 * 
	public void ViewChartAsServletStream() {
		response.setContentType("image/png");
		
		ServletOutputStream outputStream = response.getOutputStream();
		
		JFreeChart chart = getChart();
		ChartUtilities.writeChartAsJPEG(outputStream, c, 300, 400);
		
		outputStream.close();
	}
	*/
	public JFreeChart getChart() {
		// 데이터 생성
		DefaultCategoryDataset dataset1 = new DefaultCategoryDataset();  //bar chart1
		DefaultCategoryDataset dataset12 = new DefaultCategoryDataset(); //bar chart2
		DefaultCategoryDataset dataset2 = new DefaultCategoryDataset();  //line chart1

		// 데이터 입력 ( 값, 범례, 카테고리 )
		// 그래프 1
		dataset1.addValue(1.0, "S1", "1월");
		dataset1.addValue(4.0, "S1", "2월");
		dataset1.addValue(3.0, "S1", "3월");
		dataset1.addValue(5.0, "S1", "4월");
		dataset1.addValue(5.0, "S1", "5월");
		dataset1.addValue(7.0, "S1", "6월");
		dataset1.addValue(7.0, "S1", "7월");
		dataset1.addValue(8.0, "S1", "8월");
		dataset1.addValue(0, "S1", "9월");
		dataset1.addValue(0, "S1", "10월");
		dataset1.addValue(0, "S1", "11월");
		dataset1.addValue(0, "S1", "12월");
		
		// 그래프 2
		dataset12.addValue(0, "S2", "1월");
		dataset12.addValue(0, "S2", "2월");
		dataset12.addValue(0, "S2", "3월");
		dataset12.addValue(0, "S2", "4월");
		dataset12.addValue(0, "S2", "5월");
		dataset12.addValue(0, "S2", "6월");
		dataset12.addValue(0, "S2", "7월");
		dataset12.addValue(0, "S2", "8월");
		dataset12.addValue(6.0, "S2", "9월");
		dataset12.addValue(0, "S2", "10월");
		dataset12.addValue(0, "S2", "11월");
		dataset12.addValue(0, "S2", "12월");
		
		// 그래프 3(라인)
		dataset2.addValue(9.0, "T1", "1월");
		dataset2.addValue(7.0, "T1", "2월");
		dataset2.addValue(2.0, "T1", "3월");
		dataset2.addValue(6.0, "T1", "4월");
		dataset2.addValue(6.0, "T1", "5월");
		dataset2.addValue(9.0, "T1", "6월");
		dataset2.addValue(5.0, "T1", "7월");
		dataset2.addValue(4.0, "T1", "8월");
		dataset2.addValue(8.0, "T1", "9월");
		dataset2.addValue(8.0, "T1", "10월");
		dataset2.addValue(8.0, "T1", "11월");
		dataset2.addValue(8.0, "T1", "12월");
		
		// 렌더링 생성 및 세팅
		// 렌더링 생성
		final BarRenderer renderer = new BarRenderer();
		final BarRenderer renderer12 = new BarRenderer();
		final LineAndShapeRenderer renderer2 = new LineAndShapeRenderer();

		// 공통 옵션 정의
		final CategoryItemLabelGenerator generator = new StandardCategoryItemLabelGenerator();
		final ItemLabelPosition p_center = new ItemLabelPosition(ItemLabelAnchor.CENTER, TextAnchor.CENTER);
		final ItemLabelPosition p_below = new ItemLabelPosition(ItemLabelAnchor.OUTSIDE6, TextAnchor.TOP_LEFT);
		Font f = new Font("Gulim", Font.BOLD, 14);
		Font axisF = new Font("Gulim", Font.PLAIN, 14);

		// 렌더링 세팅
		// 그래프 1
		renderer.setBaseItemLabelGenerator(generator);
		renderer.setBaseItemLabelsVisible(true);
		renderer.setBasePositiveItemLabelPosition(p_center);
		renderer.setBaseItemLabelFont(f);
		// renderer.setGradientPaintTransformer(new
		// StandardGradientPaintTransformer(
		// GradientPaintTransformType.VERTICAL));
		// renderer.setSeriesPaint(0, new GradientPaint(1.0f, 1.0f, Color.white,
		// 0.0f, 0.0f, Color.blue));
		renderer.setSeriesPaint(0, new Color(0, 162, 255));

		// 그래프 2
		renderer12.setSeriesPaint(0, new Color(232, 168, 255));
		renderer12.setBaseItemLabelFont(f);
		renderer12.setBasePositiveItemLabelPosition(p_center);
		renderer12.setBaseItemLabelGenerator(generator);
		renderer12.setBaseItemLabelsVisible(true);

		// 그래프 3
		renderer2.setBaseItemLabelGenerator(generator);
		renderer2.setBaseItemLabelsVisible(true);
		renderer2.setBaseShapesVisible(true);
		renderer2.setDrawOutlines(true);
		renderer2.setUseFillPaint(true);
		renderer2.setBaseFillPaint(Color.WHITE);
		renderer2.setBaseItemLabelFont(f);
		renderer2.setBasePositiveItemLabelPosition(p_below);
		renderer2.setSeriesPaint(0, new Color(219, 121, 22));
		renderer2.setSeriesStroke(0, new BasicStroke(2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 3.0f));

		// plot 생성
		final CategoryPlot plot = new CategoryPlot();

		// plot 에 데이터 적재
		plot.setDataset(dataset1);
		plot.setRenderer(renderer);
		//plot.setDataset(1, dataset12);
		//plot.setRenderer(1, renderer12);
		//plot.setDataset(2, dataset2); //line chart
		//plot.setRenderer(2, renderer2);

		// plot 기본 설정
		plot.setOrientation(PlotOrientation.VERTICAL); // 그래프 표시 방향
		plot.setRangeGridlinesVisible(true); // X축 가이드 라인 표시여부
		plot.setDomainGridlinesVisible(true); // Y축 가이드 라인 표시여부

		// 렌더링 순서 정의 : dataset 등록 순서대로 렌더링 ( 즉, 먼저 등록한게 아래로 깔림 )
		plot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);

		// X축 세팅
		plot.setDomainAxis(new CategoryAxis()); // X축 종류 설정
		plot.getDomainAxis().setTickLabelFont(axisF); // X축 눈금라벨 폰트 조정
		plot.getDomainAxis().setCategoryLabelPositions(CategoryLabelPositions.STANDARD); // 카테고리 라벨 위치 조정

		// Y축 세팅
		plot.setRangeAxis(new NumberAxis()); // Y축 종류 설정
		plot.getRangeAxis().setTickLabelFont(axisF); // Y축 눈금라벨 폰트 조정

		// 세팅된 plot을 바탕으로 chart 생성
		final JFreeChart chart = new JFreeChart(plot);
		// chart.setTitle("Overlaid Bar Chart"); // 차트 타이틀
		// TextTitle copyright = new TextTitle("JFreeChart WaferMapPlot", new
		// Font("SansSerif", Font.PLAIN, 9));
		// copyright.setHorizontalAlignment(HorizontalAlignment.RIGHT);
		// chart.addSubtitle(copyright); // 차트 서브 타이틀
		return chart;
	}

}