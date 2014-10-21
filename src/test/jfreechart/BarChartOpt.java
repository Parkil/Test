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
 * BarChartTest.java�� ��ɺ��� ������ ����
 */
public class BarChartOpt {

	public static void main(final String[] args) throws Exception{
		String chartTitle = "��õ�� �һ籸";
		BarChartOpt demo = new BarChartOpt();
		
		Font baseFont = new Font("arial", Font.BOLD, 15); //��Ʈ���� ����ǥ�� ��Ʈ
		Font axisFont = new Font("Gulim", Font.PLAIN, 16); //x,y�� ǥ�� ��Ʈ
		
		Dataset dataset = demo.getDataSet(demo.getDummyData());
		BarRenderer renderer = demo.getRenderer(baseFont);
		CategoryPlot plot = demo.getPlot(dataset, renderer, axisFont);
		JFreeChart chart = demo.getChart(plot, chartTitle);
		
		int datacnt = ((CategoryDataset)dataset).getColumnCount();
		
		//demo.ViewChartAsFrame(chart); //JFrame ������� ǥ��
		demo.ViewChartAsPng(chart, "d:/chart.png", datacnt); //png���� ������� ����
	}
	
	/** JFrame������� ��Ʈ ǥ��
	 * @param chart
	 */
	public void ViewChartAsFrame(JFreeChart chart) {
		ChartFrame frame1 = new ChartFrame("Bar Chart", chart);
		frame1.setSize(1600, 800);
		frame1.setVisible(true);
	}
	
	/** ��Ʈ�� png�� ����
	 * @param chart JFreechart��ü
	 * @param path ����������
	 * @param datacnt ��Ʈ ������ ����
	 * @throws Exception 
	 */
	public void ViewChartAsPng(JFreeChart chart, String path, int datacnt) throws Exception{
		/*
		 * ���� 
		 * dataset���� 12�� ���� : CategoryLabelPositions.STANDARD, ũ��(1200 / 600), 
		 * dataset���� 13~20��	 : CategoryLabelPositions.UP_45, ũ��(1200 / 600),
		 * dataset���� 20�� �̻� : CategoryLabelPositions.UP_45, ũ��(1600 / 800)
		 */
		
		int width	= 1200;
		int height	= 600;
		
		if(datacnt >= 20) {
			width	= 1600;
			height	= 800;
		}
		
		ChartUtilities.saveChartAsPNG(new File(path), chart, width, height);
	}
	
	/**�ԷµǴ� �����͸� dataset�� �����Ͽ� ��ȯ
	 * @param list �Էµ�����
	 * @return
	 */
	public Dataset getDataSet(List<HashMap<String,String>> list) {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		
		for(HashMap<String,String> map : list) {
			dataset.addValue(Double.parseDouble(map.get("value")), map.get("legend"), map.get("category"));
		}
		
		return dataset;
	}
	
	/**BarRenderer��ü ��ȯ
	 * @param baseFont ��Ʈ �⺻ ��Ʈ ��ü
	 * @return
	 */
	public BarRenderer getRenderer(Font baseFont) {
		// ������ ����
		BarRenderer renderer = new BarRenderer();
		
		// ���� �ɼ� ����
		CategoryItemLabelGenerator generator = new StandardCategoryItemLabelGenerator();
		//ItemLabelPosition p_center = new ItemLabelPosition(ItemLabelAnchor.CENTER, TextAnchor.CENTER);
		ItemLabelPosition p_outsidetop = new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12, TextAnchor.BASELINE_CENTER);
		//ItemLabelPosition p_below = new ItemLabelPosition(ItemLabelAnchor.OUTSIDE6, TextAnchor.TOP_LEFT);
	
		// ������ ����
		renderer.setBaseItemLabelGenerator(generator);
		renderer.setBaseItemLabelsVisible(true);
		renderer.setBasePositiveItemLabelPosition(p_outsidetop);
		renderer.setBaseItemLabelFont(baseFont);
		renderer.setSeriesPaint(0, new Color(0, 162, 255));
		
		return renderer;
	}
	
	/**plot ��ü ��ȯ
	 * @param dataset ��Ʈ ���������� DataSet��ü
	 * @param renderer Renderer��ü
	 * @param axisFont ��Ʈ x,y�࿡ ����� ��Ʈ
	 * @return
	 */
	public CategoryPlot getPlot(Dataset dataset, CategoryItemRenderer renderer, Font axisFont) {
		// plot ����
		CategoryPlot plot = new CategoryPlot();

		// plot �� ������ ����
		plot.setDataset((CategoryDataset)dataset);
		plot.setRenderer(renderer);

		// plot �⺻ ����
		plot.setOrientation(PlotOrientation.VERTICAL); // �׷��� ǥ�� ����
		plot.setRangeGridlinesVisible(true); // X�� ���̵� ���� ǥ�ÿ���
		plot.setDomainGridlinesVisible(true); // Y�� ���̵� ���� ǥ�ÿ���

		// ������ ���� ���� : dataset ��� ������� ������ ( ��, ���� ����Ѱ� �Ʒ��� �� )
		plot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);
		
		// X�� ����
		plot.setDomainAxis(new CategoryAxis()); // X�� ���� ����
		CategoryAxis x_axis = plot.getDomainAxis();
		
		CategoryLabelPositions pos = null;
		
		//dataset������ 12�� �̻��̸� x�� ���� 45�� ������ ǥ��
		int col_cnt = ((CategoryDataset)dataset).getColumnCount();
		
		if(col_cnt <= 12) {
			pos = CategoryLabelPositions.STANDARD;
		}else {
			pos = CategoryLabelPositions.UP_45;
		}
		
		x_axis.setTickLabelFont(axisFont); // X�� ���ݶ� ��Ʈ ����
		x_axis.setCategoryLabelPositions(pos); // ī�װ� �� ��ġ ����
		x_axis.setCategoryMargin(.30); //�� �ٰ� ��������
		
		// Y�� ����
		plot.setRangeAxis(new NumberAxis()); // Y�� ���� ����
		ValueAxis y_axis = plot.getRangeAxis();
		
		y_axis.setTickLabelFont(axisFont); // Y�� ���ݶ� ��Ʈ ����
		y_axis.setLabelLocation(AxisLabelLocation.MIDDLE);
		y_axis.setLabel("�߻�ȸ��(ȸ)");
		y_axis.setLabelFont(new Font("Gulim", Font.PLAIN, 16));
		//y_axis.setLabelAngle(90 * (Math.PI / 180.0)); //�� ���� ����
		
		return plot;
	}
	
	/**JFreechar ��ü ��ȯ
	 * @param plot CategoryPlot��ü
	 * @param chartTitle ��Ʈ����
	 * @return
	 */
	public JFreeChart getChart(CategoryPlot plot, String chartTitle) {
		JFreeChart chart = new JFreeChart(plot);
		chart.removeLegend(); //legend ����
		chart.setBackgroundPaint(Color.white); //��Ʈ ���� ����
		chart.setTitle(chartTitle); //title�� ������� �ƿ� ������ ���ϸ� y�� �ֻ�� ���ڰ� �߸��� ��찡���� 
		
		return chart;
	}
	
	/**��Ʈ�� ����� ���̵����� ��ȯ
	 * @return
	 */
	public List<HashMap<String,String>> getDummyData() {
		List<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
		
		HashMap<String,String> map = null;
		for(int i = 0 ; i<24 ; i++) {
			map = new HashMap<String,String>();
			map.put("value",	String.valueOf((i+1) * 100000));
			map.put("legend",	"S1");
			map.put("category", (i+1)+"��");
			
			list.add(map);
		}
		
		return list;
	}
}