package test.jfreechart;
import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jfree.chart.ChartFrame;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLabelLocation;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.CategoryItemLabelGenerator;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.DatasetRenderingOrder;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.Dataset;
import org.jfree.ui.TextAnchor;


/*
 * BarChartTest.java를 기능별로 분할한 예제
 */
public class BarChartOpt {

	public static void main(final String[] args) throws Exception{
		String chartTitle = "부천시 소사구";
		BarChartOpt demo = new BarChartOpt();
		
		Font baseFont = new Font("arial", Font.BOLD, 15); //차트내의 숫자표시 폰트
		Font axisFont = new Font("Gulim", Font.PLAIN, 16); //x,y축 표시 폰트
		
		Dataset dataset = demo.getDataSet(demo.getDummyData());
		BarRenderer renderer = demo.getRenderer(baseFont);
		CategoryPlot plot = demo.getPlot(dataset, renderer, axisFont);
		JFreeChart chart = demo.getChart(plot, chartTitle);
		
		int datacnt = ((CategoryDataset)dataset).getColumnCount();
		
		//demo.ViewChartAsFrame(chart); //JFrame 기반으로 표시
		demo.ViewChartAsPng(chart, "d:/chart.png", datacnt); //png파일 기반으로 저장
	}
	
	/** JFrame기반으로 차트 표시
	 * @param chart
	 */
	public void ViewChartAsFrame(JFreeChart chart) {
		ChartFrame frame1 = new ChartFrame("Bar Chart", chart);
		frame1.setSize(1600, 800);
		frame1.setVisible(true);
	}
	
	/** 차트를 png로 저장
	 * @param chart JFreechart객체
	 * @param path 파일저장경로
	 * @param datacnt 차트 데이터 개수
	 * @throws Exception 
	 */
	public void ViewChartAsPng(JFreeChart chart, String path, int datacnt) throws Exception{
		/*
		 * 설정 
		 * dataset개수 12개 이하 : CategoryLabelPositions.STANDARD, 크기(1200 / 600), 
		 * dataset개수 13~20개	 : CategoryLabelPositions.UP_45, 크기(1200 / 600),
		 * dataset개수 20개 이상 : CategoryLabelPositions.UP_45, 크기(1600 / 800)
		 */
		
		int width	= 1200;
		int height	= 600;
		
		if(datacnt >= 20) {
			width	= 1600;
			height	= 800;
		}
		
		ChartUtilities.saveChartAsPNG(new File(path), chart, width, height);
	}
	
	/**입력되는 데이터를 dataset에 저장하여 반환
	 * @param list 입력데이터
	 * @return
	 */
	public Dataset getDataSet(List<HashMap<String,String>> list) {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		
		for(HashMap<String,String> map : list) {
			dataset.addValue(Double.parseDouble(map.get("value")), map.get("legend"), map.get("category"));
		}
		
		return dataset;
	}
	
	/**BarRenderer객체 반환
	 * @param baseFont 차트 기본 폰트 객체
	 * @return
	 */
	public BarRenderer getRenderer(Font baseFont) {
		// 렌더링 생성
		BarRenderer renderer = new BarRenderer();
		
		// 공통 옵션 정의
		CategoryItemLabelGenerator generator = new StandardCategoryItemLabelGenerator();
		//ItemLabelPosition p_center = new ItemLabelPosition(ItemLabelAnchor.CENTER, TextAnchor.CENTER);
		ItemLabelPosition p_outsidetop = new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12, TextAnchor.BASELINE_CENTER);
		//ItemLabelPosition p_below = new ItemLabelPosition(ItemLabelAnchor.OUTSIDE6, TextAnchor.TOP_LEFT);
	
		// 렌더링 설정
		renderer.setBaseItemLabelGenerator(generator);
		renderer.setBaseItemLabelsVisible(true);
		renderer.setBasePositiveItemLabelPosition(p_outsidetop);
		renderer.setBaseItemLabelFont(baseFont);
		renderer.setSeriesPaint(0, new Color(0, 162, 255));
		
		return renderer;
	}
	
	/**plot 객체 반환
	 * @param dataset 차트 데이터저장 DataSet객체
	 * @param renderer Renderer객체
	 * @param axisFont 차트 x,y축에 사용할 폰트
	 * @return
	 */
	public CategoryPlot getPlot(Dataset dataset, CategoryItemRenderer renderer, Font axisFont) {
		// plot 생성
		CategoryPlot plot = new CategoryPlot();

		// plot 에 데이터 적재
		plot.setDataset((CategoryDataset)dataset);
		plot.setRenderer(renderer);

		// plot 기본 설정
		plot.setOrientation(PlotOrientation.VERTICAL); // 그래프 표시 방향
		plot.setRangeGridlinesVisible(true); // X축 가이드 라인 표시여부
		plot.setDomainGridlinesVisible(true); // Y축 가이드 라인 표시여부

		// 렌더링 순서 정의 : dataset 등록 순서대로 렌더링 ( 즉, 먼저 등록한게 아래로 깔림 )
		plot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);
		
		// X축 세팅
		plot.setDomainAxis(new CategoryAxis()); // X축 종류 설정
		CategoryAxis x_axis = plot.getDomainAxis();
		
		CategoryLabelPositions pos = null;
		
		//dataset개수가 12개 이상이면 x축 라벨을 45도 각도로 표시
		int col_cnt = ((CategoryDataset)dataset).getColumnCount();
		
		if(col_cnt <= 12) {
			pos = CategoryLabelPositions.STANDARD;
		}else {
			pos = CategoryLabelPositions.UP_45;
		}
		
		x_axis.setTickLabelFont(axisFont); // X축 눈금라벨 폰트 조정
		x_axis.setCategoryLabelPositions(pos); // 카테고리 라벨 위치 조정
		x_axis.setCategoryMargin(.30); //각 바간 간격조정
		
		// Y축 세팅
		plot.setRangeAxis(new NumberAxis()); // Y축 종류 설정
		ValueAxis y_axis = plot.getRangeAxis();
		
		y_axis.setTickLabelFont(axisFont); // Y축 눈금라벨 폰트 조정
		y_axis.setLabelLocation(AxisLabelLocation.MIDDLE);
		y_axis.setLabel("발생회수(회)");
		y_axis.setLabelFont(new Font("Gulim", Font.PLAIN, 16));
		//y_axis.setLabelAngle(90 * (Math.PI / 180.0)); //라벨 각도 조정
		
		return plot;
	}
	
	/**JFreechar 객체 반환
	 * @param plot CategoryPlot객체
	 * @param chartTitle 차트제목
	 * @return
	 */
	public JFreeChart getChart(CategoryPlot plot, String chartTitle) {
		JFreeChart chart = new JFreeChart(plot);
		chart.removeLegend(); //legend 제거
		chart.setBackgroundPaint(Color.white); //차트 배경색 설정
		chart.setTitle(chartTitle); //title이 없을경우 아예 지정을 안하면 y축 최상단 문자가 잘리는 경우가있음 
		
		return chart;
	}
	
	/**차트에 사용할 더미데이터 반환
	 * @return
	 */
	public List<HashMap<String,String>> getDummyData() {
		List<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
		
		HashMap<String,String> map = null;
		for(int i = 0 ; i<24 ; i++) {
			map = new HashMap<String,String>();
			map.put("value",	String.valueOf((i+1) * 100000));
			map.put("legend",	"S1");
			map.put("category", (i+1)+"월");
			
			list.add(map);
		}
		
		return list;
	}
}