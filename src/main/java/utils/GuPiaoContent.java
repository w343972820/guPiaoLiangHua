package utils;

public class GuPiaoContent {
    String name;
    String code;
    String trade;
    public GuPiaoContent(){}
    public GuPiaoContent(String name,String code,String trade){
        this.name = name;
        this.code=code;
        this.trade = trade;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTrade() {
        return trade;
    }

    public void setTrade(String trade) {
        this.trade = trade;
    }


}
