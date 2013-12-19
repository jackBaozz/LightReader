package com.lightreader.bzz.http;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLHandshakeException;

import org.apache.http.Header;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.params.ConnRouteParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.net.wifi.WifiManager;

/***
 * Httpͨ���е�POST��GET����ʽ�Ĳ�ͬ��GET�Ѳ�������URL�ַ������棬���ݸ���������
 * ��POST�����Ĳ����Ƿ���Http�����У���Ҫ�����ֻ���Http�����ṩ�����ķ��������
 * 
 * @author hlj,bzz
 * @date
 * @versionn 1.1
 **/

@SuppressWarnings({"unused"})
public class HttpClientsUtils {
	/* ִ��downfile�󣬵õ������ļ��Ĵ�С */
	private long contentLength;
	/* ��������ʧ����Ϣ * */
	private String strResult = "�������޷����ӣ���������";
	/* http ����ͷ���� */
	private HttpParams httpParams;
	/* httpClient ���� */
	private DefaultHttpClient httpClient;

	/* �õ������� */
	private Context context;
	
	private Activity activity = null;

	public HttpClientsUtils(Activity activity) {
		this.context = activity.getBaseContext();
		this.activity = activity;
		getHttpClient();
	}

	/**
	 * �ṩGET��ʽ�ķ����������� doGet ����ʾ���� Map params=new HashMap();
	 * params.put("usename","helijun"); params.put("password","123456");
	 * httpClient.doGet(url,params)��
	 * 
	 * @param url
	 *            �����ַ
	 * @param params
	 *            �������
	 * @return ���� String jsonResult;
	 * 
	 * **/
	
	public String doGet(String url, Map params) {
		/* ����HTTPGet���� */
		String paramStr = "";
		if (params == null)
			params = new HashMap();
		/* ��������������� */
		Iterator iter = params.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			Object key = entry.getKey();
			String val = nullToString(entry.getValue());
			paramStr += paramStr = "&" + key + "=" + URLEncoder.encode(val);
		}
		if (!paramStr.equals("")) {
			paramStr = paramStr.replaceFirst("&", "?");
			url += paramStr;
		}
		return doGet(url);
	}

	/**
	 * �ṩGET��ʽ�ķ����������� doGet ����ʾ���� Map params=new HashMap();
	 * params.put("usename","gongshuanglin"); params.put("password","123456");
	 * httpClient.doGet(url,params)��
	 * 
	 * @param url
	 *            �����ַ
	 * @param params
	 *            �������
	 * @return ���� String jsonResult;
	 * 
	 */
	public String doGet(String url, List<NameValuePair> params) {
		/* ����HTTPGet���� */
		String paramStr = "";
		if (params == null)
			params = new ArrayList<NameValuePair>();
		/* ��������������� */

		for (NameValuePair obj : params) {
			paramStr += paramStr = "&" + obj.getName() + "=" + URLEncoder.encode(obj.getValue());
		}
		if (!paramStr.equals("")) {
			paramStr = paramStr.replaceFirst("&", "?");
			url += paramStr;
		}
		return doGet(url);
	}

	/**
	 * �ṩGET��ʽ�ķ����������� doGet ����ʾ����
	 * 
	 * @param url
	 *            �����ַ
	 * @return ���� String jsonResult;
	 * 
	 */
	public String doGet(String url) {
		/* ����HttpGet���� */
		HttpGet httpRequest = new HttpGet(url);
		httpRequest.setHeaders(this.getHeader());
		try {
			/* ���ֻỰSession */
			/* ����Cookie */
			MyHttpCookies li = new MyHttpCookies(context);
			CookieStore cs = li.getuCookie();
			/* ��һ������App�����CookieΪ�գ�����ʲôҲ������ֻ�е�APP��Cookie��Ϊ�յ�ʱ�����������Cooke�Ž�ȥ */
			if (cs != null) {
				httpClient.setCookieStore(li.getuCookie());
			}

			/* ���ֻỰSession end */

			/* �������󲢵ȴ���Ӧ */
			HttpResponse httpResponse = httpClient.execute(httpRequest);
			/* ��״̬��Ϊ200 ok */
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				/* ���������� */
				strResult = EntityUtils.toString(httpResponse.getEntity());

				/* ִ�гɹ�֮��õ� */
				/* �ɹ�֮��ѷ��سɹ���Cookis����APP�� */
				/* ����ɹ�֮��ÿ�ζ�����Cookis����֤ÿ�����������µ�Cookis */
				li.setuCookie(httpClient.getCookieStore());

			} else {
				strResult = "Error Response: " + httpResponse.getStatusLine().toString();
			}
		} catch (ClientProtocolException e) {
			strResult = nullToString(e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			strResult = nullToString(e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			strResult = nullToString(e.getMessage());
			e.printStackTrace();
		} finally {
			httpRequest.abort();
			this.shutDownClient();
		}
		return strResult;
	}

	/**
	 * �ṩPost��ʽ�ķ����������� Post ����ʾ���� doPost ����ʾ�� List<NameValuePair> paramlist =
	 * new ArrayList<NameValuePair>(); paramlist(new
	 * BasicNameValuePair("email","xxx@123.com")); paramlist(new
	 * BasicNameValuePair("address", "123abc"));
	 * httpClient.doPost(url,paramlist);
	 * 
	 * @param url
	 *            �����ַ
	 * @param params
	 *            �������
	 * @return ���� String jsonResult;
	 * **/

	public String doPost(String url, List<NameValuePair> params) {
		/* ����HTTPPost���� */

		HttpPost httpRequest = new HttpPost(url);
		// ��������Header��Ϣ��
		httpRequest.setHeaders(this.getHeader());
		try {
			/** ������������������� */
			// boolean upFileFlag = false;// �Ƿ����ļ��ϴ�
			// MultipartEntity mpEntity = new MultipartEntity(
			// HttpMultipartMode.BROWSER_COMPATIBLE);
			// for (NameValuePair param : params) {
			// ContentBody contentBody = null;
			// File file = new File(param.getValue());
			// if (file.isFile()) {
			// contentBody = new FileBody(file);
			// upFileFlag = true;
			// } else {
			// contentBody = new StringBody(param.getValue(), Charset
			// .forName(HTTP.UTF_8));
			// }
			// mpEntity.addPart(param.getName(), contentBody);
			// }
			//
			// if (upFileFlag == true) {// �ļ� �ϴ�
			// httpRequest.setEntity(mpEntity);
			// } else {
			/* ������������������� */
			httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			// }

			/* ���ֻỰSession */
			/* ����Cookie */
			MyHttpCookies li = new MyHttpCookies(context);
			CookieStore cs = li.getuCookie();
			/* ��һ������App�����CookieΪ�գ�����ʲôҲ������ֻ�е�APP��Cookie��Ϊ�յ�ʱ�����������Cooke�Ž�ȥ */
			if (cs != null) {
				httpClient.setCookieStore(li.getuCookie());
			}

			/* ���ֻỰSession end */
			/* �������󲢵ȴ���Ӧ */

			HttpResponse httpResponse = httpClient.execute(httpRequest);

			/* ��״̬��Ϊ200 ok */
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				/* ���������� */
				strResult = EntityUtils.toString(httpResponse.getEntity());

				/* ִ�гɹ�֮��õ� */
				/* �ɹ�֮��ѷ��سɹ���Cookis����APP�� */
				// ����ɹ�֮��ÿ�ζ�����Cookis����֤ÿ�����������µ�Cookis
				li.setuCookie(httpClient.getCookieStore());
				/* ����Cookie end */
			} else {
				strResult = "Error Response: " + httpResponse.getStatusLine().toString();
			}
		} catch (ClientProtocolException e) {
			strResult = "";
			// strResult = e.getMessage().toString();
			e.printStackTrace();
		} catch (IOException e) {
			strResult = "";
			// strResult = e.getMessage().toString();
			e.printStackTrace();
		} catch (Exception e) {
			strResult = "";
			// strResult = e.getMessage().toString();
			e.printStackTrace();
		} finally {
			httpRequest.abort();
			this.shutDownClient();
		}
		return strResult;
	}

	/** �õ� apache http HttpClient���� **/
	public DefaultHttpClient getHttpClient() {
		/* ���� HttpParams ���������� HTTP ���� */
		httpParams = new BasicHttpParams();
		/* �������ӳ�ʱ�� Socket ��ʱ���Լ� Socket �����С */
		HttpConnectionParams.setConnectionTimeout(httpParams, 20 * 1000);
		HttpConnectionParams.setSoTimeout(httpParams, 20 * 1000);
		HttpConnectionParams.setSocketBufferSize(httpParams, 8192);
		HttpClientParams.setRedirecting(httpParams, true);
		/*
		 * ����һ�� HttpClient ʵ�� //�����Զ�ѡ�����磬����Ӧcmwap��CMNET��wifi��3G
		 */
		MyHttpCookies li = new MyHttpCookies(context);
		String proxyStr = li.getHttpProxyStr();
		if (proxyStr != null && proxyStr.trim().length() > 0) {
			HttpHost proxy = new HttpHost(proxyStr, 80);
			httpClient.getParams().setParameter(ConnRouteParams.DEFAULT_PROXY, proxy);
		}
		/*
		 * ע�� HttpClient httpClient = new HttpClient(); ��Commons HttpClient
		 */
		httpClient = new DefaultHttpClient(httpParams);
		httpClient.setHttpRequestRetryHandler(requestRetryHandler);

		return httpClient;

	}

	/** �õ��豸��Ϣ��ϵͳ�汾���������� **/
	private Header[] getHeader() {
		/** ����ͷ��Ϣ end **/
		MyHttpCookies li = new MyHttpCookies(context);
		return li.getHttpHeader();
	}

	/** ����HTTPCLient **/
	public void shutDownClient() {
		httpClient.getConnectionManager().shutdown();
	}

	// /**
	// * �ṩGET��ʽ�ķ����������� doGet ����ʾ����
	// *
	// * @param url
	// * �����ַ
	// * @return ���� String jsonResult;
	// *
	// */
	// public InputStream doDownFile(String url) {
	// /** ����HttpGet���� **/
	// HttpGet httpRequest = new HttpGet(url);
	// httpRequest.setHeaders(this.getHeader());
	// try {
	// /** ���ֻỰSession **/
	// /** ����Cookie **/
	// MyHttpCookies li = new MyHttpCookies(context);
	// CookieStore cs = li.getuCookie();
	// /** ��һ������App�����CookieΪ�գ�����ʲôҲ������ֻ�е�APP��Cookie��Ϊ�յ�ʱ�����������Cooke�Ž�ȥ **/
	// if (cs != null) {
	// httpClient.setCookieStore(li.getuCookie());
	// }
	// /** ���ֻỰSession end **/
	// /* �������󲢵ȴ���Ӧ */
	// HttpResponse httpResponse = httpClient.execute(httpRequest);
	// /* ��״̬��Ϊ200 ok */
	// if (httpResponse.getStatusLine().getStatusCode() == 200) {
	// /** ִ�гɹ�֮��õ� **/
	// /** �ɹ�֮��ѷ��سɹ���Cookis����APP�� **/
	// // ����ɹ�֮��ÿ�ζ�����Cookis����֤ÿ�����������µ�Cookis
	// li.setuCookie(httpClient.getCookieStore());
	// this.contentLength = httpResponse.getEntity()
	// .getContentLength();
	// /* ���������� */
	// return httpResponse.getEntity().getContent();
	// } else {
	// strResult = "Error Response: "
	// + httpResponse.getStatusLine().toString();
	// }
	// } catch (ClientProtocolException e) {
	// strResult = e.getMessage().toString();
	// e.printStackTrace();
	// } catch (IOException e) {
	// strResult = e.getMessage().toString();
	// e.printStackTrace();
	// } catch (Exception e) {
	// strResult = e.getMessage().toString();
	// e.printStackTrace();
	// } finally {
	// // httpRequest.abort();
	// // this.shutDownClient();
	// }
	// this.contentLength = 0;
	// return null;
	// }

	/**
	 * �쳣�Զ��ָ�����, ʹ��HttpRequestRetryHandler�ӿ�ʵ��������쳣�ָ�
	 */
	private static HttpRequestRetryHandler requestRetryHandler = new HttpRequestRetryHandler() {
		// �Զ���Ļָ�����
		public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
			// ���ûָ����ԣ��ڷ����쳣ʱ���Զ�����N��
			if (executionCount >= 3) {
				// �������������Դ�������ô�Ͳ�Ҫ������
				return false;
			}
			if (exception instanceof NoHttpResponseException) {
				// ������������������ӣ���ô������
				return true;
			}
			if (exception instanceof SSLHandshakeException) {
				// ��Ҫ����SSL�����쳣
				return false;
			}
			HttpRequest request = (HttpRequest) context.getAttribute(ExecutionContext.HTTP_REQUEST);
			boolean idempotent = (request instanceof HttpEntityEnclosingRequest);
			if (!idempotent) {
				// ���������Ϊ���ݵȵģ���ô������
				return true;
			}
			return false;
		}
	};

	public long getContentLength() {
		return contentLength;
	}

	/**
	 * ����obj���� ��null����""
	 * 
	 * @param obj
	 * @return
	 */
	public static String nullToString(Object obj) {
		if (obj == null) {
			return "";
		}
		return obj.toString();
	}

	/**
	 * �ڲ���ʹ�ñ���
	 */
	/* ���õ�HTTP��ʾͷ��Ϣ */
	private static Header[] httpHeader;
	/* HTTP���ӵ�����ڵ� */
	private static String httpProxyStr;
	/* http����Ĺ���url���� */
	public static String baseurl = "http://192.168.50.56:5056/River";

	public static Header[] getHttpHeader() {
		return httpHeader;
	}

	public static void setHttpHeader(Header[] httpHeader) {
		HttpClientsUtils.httpHeader = httpHeader;
	}

	public static String getHttpProxyStr() {
		return httpProxyStr;
	}

	public static void setHttpProxyStr(String httpProxyStr) {
		HttpClientsUtils.httpProxyStr = httpProxyStr;
	}

	public static String getBaseurl() {
		return baseurl;
	}

	public static void setBaseurl(String baseurl) {
		HttpClientsUtils.baseurl = baseurl;
	}

	class MyHttpCookies {
		// ÿҳ������ʾ�����
		private int pageSize = 10;
		// ��ǰ�Ự���cokie��Ϣ
		private CookieStore uCookie = null;
		// �����Ķ���
		Context context;

		public MyHttpCookies(Context context) {
			this.context = context;
			// y��������ͷ
			Header[] header = { new BasicHeader("PagingRows", String.valueOf(pageSize)) };
			httpHeader = header;
		}

		/**
		 * �����Զ�ѡ�����磬����Ӧcmwap��CMNET��wifi��3G
		 */
		@SuppressWarnings("static-access")
		public void initHTTPProxy() {
			WifiManager wifiManager = (WifiManager) (context.getSystemService(context.WIFI_SERVICE));
			if (!wifiManager.isWifiEnabled()) {
				Uri uri = Uri.parse("content://telephony/carriers/preferapn"); // ��ȡ��ǰ����ʹ�õ�APN�����
				Cursor mCursor = context.getContentResolver().query(uri, null, null, null, null);
				if (mCursor != null) {
					mCursor.moveToNext(); // �α�������һ����¼����ȻҲֻ��һ��
					httpProxyStr = mCursor.getString(mCursor.getColumnIndex("proxy"));
				}
			} else {
				httpProxyStr = null;
			}
		}

		public int getPageSize() {
			return pageSize;
		}

		public void setPageSize(int pageSize) {
			this.pageSize = pageSize;
		}

		public CookieStore getuCookie() {
			return uCookie;
		}

		public void setuCookie(CookieStore uCookie) {
			this.uCookie = uCookie;
		}

		public Header[] getHttpHeader() {
			return httpHeader;
		}

		public String getHttpProxyStr() {
			return httpProxyStr;
		}
	}

}
