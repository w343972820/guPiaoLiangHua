package vicoMain;

public class VicoTest {
    public static void main(String[] args) {
        String code ="var hq_str_sh601006=\"大秦铁路,6.580,6.580,6.580,6.600,6.570,6.580,6.590,10869409,71611341.000,1275200,6.580,456300,6.570,578700,6.560,617400,6.550,252200,6.540,1059600,6.590,1226230,6.600,1052110,6.610,525100,6.620,495800,6.630,2021-06-28,10:26:34,00,\";";
        String[] fengeshu = code.split("\"");
        String[] douhaofenge = fengeshu[1].split(",");
        for(String xiao:douhaofenge){
            System.out.println(xiao);
        }
                


    }
}
