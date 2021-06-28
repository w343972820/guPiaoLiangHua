package utils;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class HttpUtils {
	public static String doGet(String httpurl) {
		HttpURLConnection connection = null;

		InputStream is = null;
		BufferedReader br = null;
		String result = null;// 返回结果字符�?
		try {
			// 创建远程url连接对象
			URL url = new URL(httpurl);
			// 通过远程url连接对象打开�?个连接，强转成httpURLConnection�?
			connection = (HttpURLConnection) url.openConnection();
			// 设置连接方式：get
			connection.setRequestMethod("GET");
			// 设置连接主机服务器的超时时间�?15000毫秒
			connection.setConnectTimeout(10000);
			// 设置读取远程返回的数据时间：60000毫秒
			connection.setReadTimeout(15000);
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 发�?�请�?
			connection.connect();



            // 通过connection连接，获取输入流
			if (connection.getResponseCode() == 200) {


				is = connection.getInputStream();
				// 封装输入流is，并指定字符�?
                br = new BufferedReader(new InputStreamReader(connection.getInputStream(),"GBK"));

				//br = new BufferedReader(new InputStreamReader(is, "utf-8"));
				// 存放数据
				StringBuffer sbf = new StringBuffer();
				String temp = null;
				while ((temp = br.readLine()) != null) {
					sbf.append(temp);
					sbf.append("\r\n");
				}
                //String par = sbf.toString().replaceAll("%(?![0-9a-fA-F]{2})", "%25");
                //result = URLDecoder.decode(par, "UTF-8");
                //result = new String(result.getBytes("GBK"),"utf-8");
                result = sbf.toString();


			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// 关闭资源
			if (null != br) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			if (null != is) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			connection.disconnect();// 关闭远程连接
		}

		return result;
	}

    public static String doGet2(String httpurl) {
        HttpURLConnection connection = null;

        InputStream is = null;
        BufferedReader br = null;
        String result = null;// 返回结果字符�?
        try {
            // 创建远程url连接对象
            URL url = new URL(httpurl);
            // 通过远程url连接对象打开�?个连接，强转成httpURLConnection�?
            connection = (HttpURLConnection) url.openConnection();
            // 设置连接方式：get
            connection.setRequestMethod("GET");
            // 设置连接主机服务器的超时时间�?15000毫秒
            connection.setConnectTimeout(10000);
            // 设置读取远程返回的数据时间：60000毫秒
            connection.setReadTimeout(15000);
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发�?�请�?
            connection.connect();



            // 通过connection连接，获取输入流
            if (connection.getResponseCode() == 200) {


                is = connection.getInputStream();
                // 封装输入流is，并指定字符�?
                //br = new BufferedReader(new InputStreamReader(connection.getInputStream(),"GBK"));

                br = new BufferedReader(new InputStreamReader(is, "utf-8"));
                // 存放数据
                StringBuffer sbf = new StringBuffer();
                String temp = null;
                while ((temp = br.readLine()) != null) {
                    sbf.append(temp);
                    sbf.append("\r\n");
                }
                //String par = sbf.toString().replaceAll("%(?![0-9a-fA-F]{2})", "%25");
                //result = URLDecoder.decode(par, "UTF-8");
                //result = new String(result.getBytes("GBK"),"utf-8");
                result = sbf.toString();


            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            if (null != br) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            connection.disconnect();// 关闭远程连接
        }

        return result;
    }

	public static String post(String strURL, String params) {
        //System.out.println(strURL);
        //System.out.println(params);
        BufferedReader reader = null;
        try {
            URL url = new URL(strURL);// 创建连接
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            connection.setRequestMethod("POST"); // 设置请求方式
            // connection.setRequestProperty("Accept", "application/json"); // 设置接收数据的格�?
            connection.setRequestProperty("Content-Type", "application/json"); // 设置发�?�数据的格式
            connection.connect();
            //�?定要用BufferedReader 来接收响应， 使用字节来接收响应的方法是接收不到内容的
            OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream(), "UTF-8"); // utf-8编码
            if (params != null) {
            	out.append(params);
            }
            out.flush();
            out.close();
            // 读取响应
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            String line;
            String res = "";
            while ((line = reader.readLine()) != null) {
                res += line;
            }
            reader.close();
            return res;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null; // 自定义错误信�?
    }
	public static String post(String strURL, Map<String,Object> params) {
        System.out.println(strURL);
        System.out.println(params);
        BufferedReader reader = null;
        Iterator<Entry<String, Object>> it = params.entrySet().iterator();
        StringBuilder reqstr = new StringBuilder();
        while(it.hasNext()) {
        	Entry<String, Object> value = it.next();
        	reqstr.append(value.getKey()+"="+value.getValue());
        	if(it.hasNext()) {
        		reqstr.append("&");
        	}
        }
        System.out.println("reqstr:"+reqstr);
        try {
            URL url = new URL(strURL);// 创建连接
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            connection.setRequestMethod("POST"); // 设置请求方式
            // connection.setRequestProperty("Accept", "application/json"); // 设置接收数据的格�?
            connection.setRequestProperty("Content-Type", "application/json"); // 设置发�?�数据的格式
            connection.connect();
            //�?定要用BufferedReader 来接收响应， 使用字节来接收响应的方法是接收不到内容的
            OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream(), "UTF-8"); // utf-8编码
            if (params != null) {
            	out.append(reqstr.toString());
            }
            out.flush();
            out.close();
            // 读取响应
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            String line;
            String res = "";
            while ((line = reader.readLine()) != null) {
                res += line;
            }
            reader.close();
            return res;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null; // 自定义错误信�?
    }
}
