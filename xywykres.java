import org.jfree.ui.ApplicationFrame; 
import java.awt.Color; 
import java.awt.BasicStroke; 
import org.jfree.chart.ChartPanel; 
import org.jfree.chart.JFreeChart; 
import org.jfree.data.xy.XYDataset; 
import org.jfree.data.xy.XYSeries; 
import org.jfree.ui.RefineryUtilities; 
import org.jfree.chart.plot.XYPlot; 
import org.jfree.chart.ChartFactory; 
import org.jfree.chart.plot.PlotOrientation; 
import org.jfree.data.xy.XYSeriesCollection; 
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import java.util.Arrays;
import org.jfree.chart.annotations.XYShapeAnnotation;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import org.jfree.chart.annotations.XYTextAnnotation;


 class wykres extends ApplicationFrame{
	
	private final XYPlot plot;
	private Color colors[]={Color.RED, Color.GREEN, Color.BLUE, Color.BLACK, Color.ORANGE, Color.MAGENTA, Color.LIGHT_GRAY, Color.PINK};
	private BasicStroke stroke;
	private XYLineAndShapeRenderer renderer;
	private String[] oz_serii;
	private XYSeries serie_tab[];
	private ChartPanel chartPanel;
	int width_o=640;
	int height_o=480; 
	double scale_o=(double)width_o/ (double) height_o;
	
	
 public wykres(String tytul, String tytulwykresu, String oz_x, String oz_y, double dane[][], String[] serie) {
      super(tytul);
	  oz_serii=serie;
	  serie_tab = new XYSeries[oz_serii.length];
	  
	  
      JFreeChart xylineChart = ChartFactory.createXYLineChart(
         tytulwykresu ,
         oz_x ,
         oz_y ,
         sformatuj_dane(dane),
         PlotOrientation.VERTICAL ,
         true , true , false);
      
	  xylineChart.setBackgroundPaint(Color.WHITE);
      chartPanel = new ChartPanel(xylineChart);
      chartPanel.setPreferredSize( new java.awt.Dimension( width_o , height_o));
      chartPanel.setDomainZoomable(false);
	  plot = xylineChart.getXYPlot();
	  plot.setDomainGridlinePaint(Color.BLACK);
	  plot.setRangeGridlinePaint(Color.BLACK);
	  plot.setBackgroundPaint(Color.WHITE);
	  
	 

	  
      renderer = new XYLineAndShapeRenderer();
	  
	  //renderer.setDefaultStroke(new BasicStroke( 2.0f ));
	  
	  for(int i=0; i<dane.length-1; i++){
		renderer.setSeriesPaint(i , colors[i]);   
	  }
	  
      plot.setRenderer(renderer); 
      setContentPane(chartPanel);
	  
 
   }
   
   void zakres_y(double ymin, double ymax){
   plot.getRangeAxis(0).setRange(ymin,ymax);
   }
   
   void zakres_x(double xmin, double xmax){
   plot.getDomainAxis(0).setRange(xmin ,xmax);
   }
   
   
   void ustaw_markery(boolean flag){
	   for (int i=0; i<serie_tab.length; i++){
	  renderer.setSeriesShapesVisible(i, flag);
	   }  
   }
   
   void ustaw_linie(float szer, float pelne){
	   float[] dashes={pelne};
	   BasicStroke stroke=new BasicStroke(szer, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER, 10.0f, dashes, 0.0f);
	    for (int i=0; i<serie_tab.length; i++){
	  renderer.setSeriesStroke(i, stroke ,true);
	   } 
   }
   
void ustaw_konce(){
	  XYDataset dset= plot.getDataset(0);
	  double d_xx=0.06;
	  double d_yy=d_xx*scale_o;
	  //ilosc elementow w serii
	  int i_n=dset.getItemCount(0);
	  double x_=0.0;
	  double y_=0.0;
	  double x__=0.0;
	  double y__=0.0;
	  for (int i=0; i<oz_serii.length; i++){
		  x_=dset.getXValue(i,0);
		  x__=dset.getXValue(i,i_n-1);
		  y_=dset.getYValue(i,0);
		  y__=dset.getYValue(i,i_n-1);
		  Ellipse2D el= new Ellipse2D.Double(x_-d_xx/2.0,y_ - d_yy/2.0,d_xx,d_yy);
		  Ellipse2D el_= new Ellipse2D.Double(x__-d_xx/2.0,y__ - d_yy/2.0,d_xx,d_yy);
		  XYShapeAnnotation anno=new XYShapeAnnotation(el, new BasicStroke(2.0f), colors[i], Color.WHITE);
		  XYShapeAnnotation anno_=new XYShapeAnnotation(el_, new BasicStroke(2.0f), colors[i], Color.WHITE);
		  plot.addAnnotation(anno);
		  plot.addAnnotation(anno_);
		 
	  }
	  //Rectangle2D d=new Rectangle2D.Double(4.0,25.0,0.075,0.075*(scale_o));
	  }
	  
void ustaw_etykiety(String poczatek, String koniec, int nr_serii){
	XYDataset dset= plot.getDataset(0);
	//ilosc elementow w serii
	int i_n=dset.getItemCount(0);
	int i=nr_serii;
	double x1; double x2; double y1; double y2;
	x1=dset.getXValue(i,0);
	x2=dset.getXValue(i,i_n-1);
	y1=dset.getYValue(i,0);
	y2=dset.getYValue(i,i_n-1);
	XYTextAnnotation text1=new XYTextAnnotation(poczatek, x1, y1);
	XYTextAnnotation text2=new XYTextAnnotation(koniec, x2, y2);
	//text1.setFont(new Font("",Font.ITALIC, 12));
	//text2.setFont(new Font("",Font.ITALIC, 12));
}	  
	  
void ustaw_strzalki(int ilosc, int nr_serii, boolean odwrotnie){
	XYDataset dset= plot.getDataset(0);
	//wysokosc trojkata
	double h=0.15;
	//podstawa trojkata
	double b=h*0.75;
	//polowa podstawy trojkata
	double b_2=b/2.0;
	//ilosc elementow w serii
	int i_n=dset.getItemCount(0);
	int przedzial=i_n/(ilosc+1);
	int indeks=0;
	int indeksp1=0;
	//mniejsza zmienna do przechowywania indeksu serii
	int k = nr_serii-1;
	//dla n-strzalek
	double x_1=0.0;
	double x_2=0.0;
	double y_1=0.0;
	double y_2=0.0;
	for (int i=0; i<ilosc;i++){
		indeks=przedzial*(i+1);
		indeksp1=indeks+1;
		//z dwóch punktów będzie obliczony wektor
		x_1=dset.getXValue(k,indeks);
		y_1=dset.getYValue(k,indeks);
		x_2=dset.getXValue(k,indeksp1);
		y_2=dset.getYValue(k,indeksp1);
		
		double vec_len=Math.sqrt((x_2-x_1)*(x_2-x_1)+(y_2-y_1)*(y_2-y_1));
		double[] wektor={(x_2-x_1)/vec_len, (y_2-y_1)/vec_len}; //wektor jednostkowy
		//wektor prostopadły do wektora jednostkowego - rot. o 90 st. w lewo
		double[] wek_pro={wektor[0]*0.0+wektor[1]*-1.0, wektor[0]*1.0+wektor[1]*0.0};
		//Punkty trójkąta
		Point2D wierzcholek=new Point2D.Double(x_1+wektor[0]*h, y_1+wektor[1]*h);
		Point2D wierz_2=new Point2D.Double(x_1+wek_pro[0]*b_2, y_1+wek_pro[1]*b_2);
		Point2D wierz_3=new Point2D.Double(x_1-wek_pro[0]*b_2, y_1-wek_pro[1]*b_2);
		
		//nowy trójkąt
		TriangleShape ts=new TriangleShape(wierzcholek, wierz_2, wierz_3);
		XYShapeAnnotation an1=new XYShapeAnnotation(ts, new BasicStroke(2.0f), colors[k], colors[k]);
		plot.addAnnotation(an1);
	}
	
}

XYSeriesCollection sformatuj_dane(double dane[][]){
	final XYSeriesCollection dataset = new XYSeriesCollection();
	
for (int i=0; i<dane.length-1; i++){
	serie_tab[i]=new XYSeries(oz_serii[i]);
	for (int k=0; k<dane[0].length; k++){
		serie_tab[i].add(dane[0][k], dane[i+1][k]);
	}
	dataset.addSeries(serie_tab[i]);
}		
		return dataset;
}
	

	

   void rysuj(){
	  ustaw_markery(false);
	  ustaw_konce();
	  ustaw_strzalki(3, 1, false);
	  ustaw_linie(4.0f, 1.0f);
	  pack();          
      RefineryUtilities.centerFrameOnScreen(this);
      setVisible(true);
   }
}

class TriangleShape extends Path2D.Double {
  public TriangleShape(Point2D... points) {
    moveTo(points[0].getX(), points[0].getY());
    lineTo(points[1].getX(), points[1].getY());
    lineTo(points[2].getX(), points[2].getY());
    closePath();
}}