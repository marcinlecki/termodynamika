import java.util.Arrays;
import static java.lang.Math.*;


public class wymiennik_krew{
public static void main(String[] args){
//założone parametry termofizyczne krwi
double rho=1050.0; double c=3740.0; 
//parametry termofizyczne wody w temp. 280K	
double rho_w=1000.0; double c_w=4198.0;
//natężenie przepływu krwi
double V_k=5.0; // [l/min]
//temperatury krwi
double t_ki=37.0; double t_ko=25.0;
//temperatury wody
double t_wi=0.0; double t_wo=15.0; 
//Współczynnik przenikania ciepła
double U=750.0;
//obliczenie strumienia masy krwi
double m_k=rho*konwersja.l_min_na_SI(V_k);
 System.out.printf("Strumień masy krwi wynosi: %.4f [kg/s] \n", m_k);
 //obliczenie róźnic temperatur strumieni cieczy
double deltaTk=t_ki-t_ko;
double deltaTw=t_wo-t_wi;
//Strumień ciepła przekazywany przez krew
double Qk=m_k*c*deltaTk;
 System.out.printf("Strumień ciepła przekazywany przez krew: %.0f [W] \n", Qk);
//Strumień masy wody lodowej
double m_w=Qk/(c_w*deltaTw);
System.out.printf("Strumień masy wody lodowej: %.4f [W] \n", m_w);
//Natężenie przepływu wody lodowej 
System.out.printf("Natężenie przepływu wody lodowej: %.4f [l/min] \n", konwersja.SI_na_l_min(m_w/rho_w));
//Pojemności cieplne strumieni
double C_k=m_k*c; double C_w=m_w*c_w;
System.out.printf("Pojemności cieplne strumienia wody: %.0f [W/K] i krwi: %.0f [W/K] \n", C_w, C_k);
//minimalna wartość pojemności cieplnej strumienia
double C_min=min(C_w, C_k);
System.out.printf("Minimalna wartość pojemności cieplnej strumienia: %.0f [W/K] \n", C_min);
//maksymalny osiągalny strumień ciepła
double Qmax=C_min*(t_ki-t_wi);
System.out.printf("Maksymalny osiągalny strumień ciepła: %.0f [W] \n", Qmax);
//parametry potrzebne do zastosowania metody e-NTU
double epsilon = Qk/Qmax;
double C_max=max(C_w, C_k);
double C_r=C_min/C_max;
//obliczenie NTU
double NTU=obliczenia.NTU_2nm(epsilon,C_r, 0.1);
System.out.printf("NTU: %.3f [-] \n", NTU);
//obliczenie powierzchni
double A = NTU*(C_min/U);
System.out.printf("Powierzchnia wymiany ciepła: %.3f [m^2] \n", A);
double V_w=2.00;
//tablica przechowująca wartości przepływów, dla których wykonany będzie wykres
double[] Vws=new double[41];
double[][] data=new double[2][41];
data[0]=Vws;

for(int i=0; i<=40; i=i+1){
	Vws[i]=V_w;
	V_w=V_w+0.1;
	data[1][i]=oblicz_temp(V_w, V_k, rho, rho_w, c,  c_w,  A,  U, t_wi, t_ki)[1];
	
}

String[] serie={"Temp. wylotowa krwi"};
String[] twyk={"Aplikacja", "t=f(V)", "V", "t"};
wykres wykres1 = new wykres(twyk[0], twyk[1], twyk[2], twyk[3], data, serie);
wykres1.zakres_y(23.0, 27.0);
wykres1.ustaw_markery(false);
wykres1.rysuj();


double[] wynik = oblicz_temp(V_w, V_k, rho, rho_w, c,  c_w,  A,  U, t_wi, t_ki);


//System.out.printf("Temp. wylotowa wody: %.2f oC, temp. wylotowa krwi: %.2f oC, strumień ciepła: %.2f, NTU: %.4f. \n", wynik[0], wynik[1], wynik[2], wynik[3]);
} 


	
static double[] oblicz_temp(double V_w, double V_k, double rho, double rhow, double c, double cw, double A, double U, double twi, double tki){
	//obliczenie strumienia masy krwi
	double m_k=rho*konwersja.l_min_na_SI(V_k);
	double m_w=rhow*konwersja.l_min_na_SI(V_w);
	double C_k=m_k*c; double C_w=m_w*cw;
	double C_min=min(C_w, C_k);
	double C_max=max(C_w, C_k);
	double C_r=C_min/C_max;
	//maksymalny osiągalny strumień ciepła
	double Qmax=C_min*(tki-twi);
	double NTU=(U*A)/C_min;
	//System.out.printf("Sprawdzenie składowych: %.3f, %.3f, %.3f \n", U,A, C_min);
	double epsilon=obliczenia.epsilon_NTU(C_r,NTU);
	double Q=epsilon *Qmax;
	double Twyl_w=twi+Q/C_w;
	double Twyl_k=tki-Q/C_k;
	return new double[] {Twyl_w, Twyl_k, Q, NTU};
}
	
}	


class obliczenia{
static double NTU_2nm(double eps, double C_r, double NTU){
int k=1;
double NTU_p=NTU;
double NTU_k=NTU;
double xi=0.33;
do{
//System.out.println(k +":");
NTU_p=NTU_k;
//System.out.printf("NTU_p: %.3f \n", NTU_p);
NTU_k=pow((C_r*log(1.0-eps))/(exp(-C_r*pow(NTU_p,0.78))-1.0), 1.0/0.22);
NTU_k=NTU_k*xi+(1.0-xi)*NTU_p;
//System.out.printf("NTU_k: %.3f \n", NTU_k);
//System.out.println(100.0*(NTU_k-NTU_p)/NTU_p);
if(k>25) break;
k=k+1;
}while (Math.abs(100.0*((NTU_k-NTU_p)/NTU_p))>0.1);
return NTU_k;		
}	
	
static double epsilon_NTU(double C_r, double NTU){
	return 1.0 - exp((1.0/C_r)*pow(NTU, 0.22)*(exp(-C_r*Math.pow(NTU,0.78))-1.0));
}

	
}

class konwersja{

static double l_min_na_SI(double litry){
	return litry*(1.0e-3/60.0);
	
}	

static double SI_na_l_min(double m3){
	return m3*(1.0e3*60.0);
}
	
}