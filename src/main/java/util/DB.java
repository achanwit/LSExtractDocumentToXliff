package util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Properties;

import javax.servlet.ServletContext;

import com.asiaonline.info.Server;
import model.ServletContextMock;

public class DB {
	public String driver = "";
	public String connectionString = "";
	public int connectionTimeout = 300;// seconds
	private String configConnectionString = "";
	private Properties _oProperty = null;
	public String _sPropertyPath = "";
	private Connection oConn = null;
	private String pathDBProperties = "";
	private String errormessage = "An unexpected database issue occurred. Please contact your system administrator.";


	public DB() {
		// TODO Auto-generated constructor stub
	}
	
	public Exception writeError(Exception ex) throws ClassNotFoundException,
	InstantiationException, IllegalAccessException, SQLException,
	IOException {
Exception exOutput = ex;
try {
	String sDBErrorPath = processGetLookupDataRegistry("General",
			"DBErrorPath");
	
	if (sDBErrorPath.equals("")) {
		if (Server.isWindows())
			sDBErrorPath = "C:\\temp\\lse-logs\\database";
		else
			sDBErrorPath = "/var/www/logs/lse-logs/database";
	}
	File dir = new File(sDBErrorPath);
	if (!dir.exists()) {
		boolean cancreate = dir.mkdirs();
	}

	Calendar oCal = Calendar.getInstance();
	SimpleDateFormat oDateTimeFormat = new SimpleDateFormat("yyyyMMdd");
	String filename = oDateTimeFormat.format(oCal.getTime());
	String errorpath = combine(sDBErrorPath, filename + ".txt");

	OutputStreamWriter writer = new OutputStreamWriter(
			new FileOutputStream(errorpath, true), "UTF-8");
	BufferedWriter fbw = new BufferedWriter(writer);
	fbw.write(oCal.getTime()+" "+getStackTrace(ex) + "\n");
	fbw.close();

	// case Data truncation: Data too long for column 'firstname' at row
	// 1
	if (ex.getMessage().contains("Data too long for column")) {
		try {
			String[] aCol = ex.getMessage().split("'");
			String msg = aCol[1];
			errormessage = "Data too long for " + msg;
		} catch (Exception e) {
			// TODO: handle exception
			errormessage = ex.getMessage();
		}
	}

	exOutput = new Exception(errormessage);
} catch (Exception e) {
	// TODO: handle exception
}
return exOutput;
}

/*
* public interface Cache { Object get(final Object key);
* 
* Object put(final Object key, final Object value); }
*/

public DB(String typeName, String lookupKey) throws Exception {
ProcessDB(typeName, lookupKey, null, null);
}

public DB(String typeName, String lookupKey, ServletContextMock app)
	throws Exception {
	long Begin = System.currentTimeMillis();
ProcessDB(typeName, lookupKey, app, null);
long End = System.currentTimeMillis();
long Total = End-Begin;
System.out.println("Total Process DB: "+Total);
}

public DB(String typeName, String lookupKey, ServletContextMock app,
	String _pathDBProperties) throws Exception {
pathDBProperties = _pathDBProperties;
ProcessDB(typeName, lookupKey, app, pathDBProperties);
}

public String checkEmpty(Object object) {
if (object == null)
	return "";

return object.toString();
}
public String getHashMapValue2(HashMap<String, String> hashMapObject,
	String columnName) {
if (hashMapObject != null && hashMapObject.get(columnName) != null) {
	String val = hashMapObject.get(columnName);
	if (val.equals("true"))
		val = "1";
	else if (val.equals("false"))
		val = "0";
	else if (val.trim().equals(""))
		return null;
	return val;
}
return null;
}

public Boolean getHashMapValue3(HashMap<String, String> hashMapObject,
	String columnName) {
if (hashMapObject != null && hashMapObject.get(columnName) != null) {
	String val = hashMapObject.get(columnName);
	if (val.toLowerCase().equals("true") || val.equals("1"))
		return true;
	else if (val.toLowerCase().equals("false") || val.equals("0"))
		return false;
}
return false;
}
public String getValue2(String val) {
if (val == null)
	return null;
else if (val.equals(""))
	return null;
else if (val.toLowerCase().trim().equals("null")
		|| val.toLowerCase().trim().equals("'null'"))
	return null;
return val;
}

public Boolean getValue3(String val) {
if (val == null)
	return false;
else if (val.equals(""))
	return false;
if (val.toLowerCase().equals("true") || val.equals("1"))
	return true;
else if (val.toLowerCase().equals("false") || val.equals("0"))
	return false;
return false;
}

public String getValue4(String val) {
if (val == null)
	return null;
else if (val.equals(""))
	return "";
else if (val.toLowerCase().trim().equals("null")
		|| val.toLowerCase().trim().equals("'null'"))
	return null;
return val;
}

private void ProcessDB(String typeName, String lookupKey,
	ServletContextMock app, String pathDBProperties) throws Exception {
// final Cacheonix cacheManager = Cacheonix.getInstance();
// final Map cache = cacheManager.getCache("invoce.cache");

// get from cache
String cacheName = checkEmpty(typeName).trim().toLowerCase() + "_"
		+ checkEmpty(lookupKey).trim().toLowerCase();
if (app != null && app.getAttribute("db_driver") != null
		&& app.getAttribute(cacheName) != null) {

	// driver= cache.get("db_driver").toString();
	// connectionString= cache.get("db_connectionstring").toString();

	driver = app.getAttribute("db_driver").toString();
	connectionString = app.getAttribute(cacheName).toString();
	return;
} else {

}

_sPropertyPath = getApplicationDir();
String filePath = "";
if (pathDBProperties == null || pathDBProperties.equals("")) {
	if (Server.isWindows())
		filePath = _sPropertyPath + "\\db.properties";
	else
		filePath = "/var/www/lse/db.properties";
} else
	filePath = pathDBProperties;
File file = new File(filePath);
boolean exists = file.exists();
/*
 * if (!exists) { CreatePropertyFile(); }
 */
LoadProperty();

// get driver & connection string of config db
driver = getValue("driver");
configConnectionString = getValue("connectionstring");
Class.forName(driver).newInstance();

//
ArrayList<HashMap<String, Object>> output = GetRegistryByKeyReturnLookupData(
		typeName, lookupKey);

for (int i = 0; i < output.size(); i++) {
	HashMap<String, Object> oRow = (HashMap<String, Object>) output
			.get(i);
	connectionString = oRow.get("lookupvalue") == null ? "" : oRow.get(
			"lookupvalue").toString();
}

// keep in cache
if (app != null && !connectionString.equals("")) {
	app.setAttribute("db_driver", driver);
	app.setAttribute(cacheName, connectionString);

	// Put object to the cache
	// cache.put("db_driver", "driver");

	// cache.put("db_connectionstring", connectionString);
	// Get object from the cache

}
}

public String getValue(String sKey) {
return _oProperty.getProperty(sKey, "");
}

public void LoadProperty() {
try {
	// Read config file
	String path = "";
	if (pathDBProperties == null || pathDBProperties.equals("")) {
		if (Server.isWindows())
			path = _sPropertyPath + "\\db.properties";
		else
			path = "/var/www/lse/db.properties";
	} else
		path = pathDBProperties;

	FileInputStream input = new FileInputStream(path);
	_oProperty = new Properties();
	_oProperty.loadFromXML(input);

} catch (Exception e) {
	e.printStackTrace();
}
}

public void CreatePropertyFile() {
FileOutputStream oStream = null;

try {
	String path = "";
	if (pathDBProperties == null || pathDBProperties.equals("")) {
		if (Server.isWindows())
			path = _sPropertyPath + "\\db.properties";
		else
			path = "/var/www/lse/db.properties";
	} else
		path = pathDBProperties;

	oStream = new FileOutputStream(path);
	_oProperty = new Properties();
	_oProperty.put("connectionstring",
			"[Please Enter Connection String]");// e.g.
												// "jdbc:mysql://192.168.69.83/aov3?user=root&password=1qaz2wsx"
	_oProperty.put("driver", "[Please Enter Driver]");// e.g.
														// org.gjt.mm.mysql.Driver
	_oProperty.storeToXML(oStream, "DB Config", "UTF-8");
} catch (Exception e) {
	e.printStackTrace();
} finally {
	if (oStream != null) {
		try {
			oStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
}

public String getApplicationDir() {

String applicationDir = getClass().getProtectionDomain()
		.getCodeSource().getLocation().getPath();

if (applicationDir.endsWith(".exe")) {
	applicationDir = new File(applicationDir).getParent();
} else {
	// Add the path to the class files
	// applicationDir += getClass().getName().replace('.', '/');

	// Step one level up as we are only interested in the
	// directory containing the class files
	applicationDir = new File(applicationDir).getParent();
}
return applicationDir;

}

public ArrayList<HashMap<String, Object>> GetRegistryByKeyReturnLookupData(
	String serviceName, String lookupKey)
	throws ClassNotFoundException, InstantiationException,
	IllegalAccessException, SQLException {
return processGetRegistryByKeyReturnLookupData(null, serviceName,
		lookupKey);
}

public ArrayList<HashMap<String, Object>> GetRegistryByKeyReturnLookupData(
	String parentServiceName, String serviceName, String lookupKey)
	throws ClassNotFoundException, InstantiationException,
	IllegalAccessException, SQLException {
return processGetRegistryByKeyReturnLookupData(parentServiceName,
		serviceName, lookupKey);
}

// get ข้อมูลใน table registry ตาม key
private ArrayList<HashMap<String, Object>> processGetRegistryByKeyReturnLookupData(
	String parentServiceName, String serviceName, String lookupKey)
	throws ClassNotFoundException, InstantiationException,
	IllegalAccessException, SQLException {
ResultSet oResultSet = null;
String sCommandText = "";
ArrayList<HashMap<String, Object>> alData = new ArrayList<HashMap<String, Object>>();
oConn = DriverManager.getConnection(configConnectionString);
DriverManager.setLoginTimeout(connectionTimeout);
try {
	sCommandText = " SELECT distinct g.lookupvalue FROM generallookup g ";
	sCommandText += " inner join generallookuptype t1 on   g.generallookuptypeid=t1.generallookuptypeid ";
	if (parentServiceName != null && parentServiceName != "")
		sCommandText += " inner join generallookuptype t2 on t2.typeid=t1.typesubid ";
	sCommandText += " where t1.typename='" + serviceName + "' ";
	if (parentServiceName != null && parentServiceName != "")
		sCommandText += " and t2.typename='" + parentServiceName + "' ";
	sCommandText += " and g.lookupkey='" + lookupKey + "' ";

	Statement oStatement = oConn.createStatement();
	oResultSet = oStatement.executeQuery(sCommandText);

	ResultSetMetaData oMeta = oResultSet.getMetaData();
	int columns = oMeta.getColumnCount();

	while (oResultSet.next()) {
		HashMap<String, Object> oRow = new HashMap<String, Object>();

		for (int i = 1; i <= columns; i++) {
			oRow.put(oMeta.getColumnLabel(i), oResultSet.getObject(i));
		}
		alData.add(oRow);
	}
} catch (SQLException ex) {
	throw ex;
} finally {
	if (oResultSet != null)
		oResultSet.close();
	if (oConn != null)
		oConn.close();
}
return alData;
}

public String checkSQLInjection(String sqlInput) {
String sqlOutput = sqlInput;

sqlOutput = sqlOutput.trim().replace("'", "''");
sqlOutput = sqlOutput.trim().replace("\\", "\\\\");

return sqlOutput;

}

public String combine(String path1, String path2) {
File file1 = new File(path1);
File file2 = new File(file1, path2);
String path = file2.getPath();
if (Server.isWindows())
	path = path.replace("/", "\\");
else
	path = path.replace("\\", "/");
return path;
}

public boolean checkMount(String mountPath) throws Exception {

String s = "";
try {
	if (Server.isWindows())
		return true;
	else {
		String command = "df -P -T " + mountPath;

		Process p = Runtime.getRuntime().exec(command);
		BufferedReader stdInput = new BufferedReader(
				new InputStreamReader(p.getInputStream()));

		BufferedReader stdError = new BufferedReader(
				new InputStreamReader(p.getErrorStream()));

		// read the output from the command

		while ((s = stdInput.readLine()) != null) {
			if (s.trim().toLowerCase().contains("nfs")) {
				return true;
			}
		}

		// read any errors from the attempted command
		while ((s = stdError.readLine()) != null) {
			if (!s.trim().equals("")) {
				throw new Exception("Error method CheckMount:" + s);
			}
		}
	}

} catch (IOException e1) {
	throw e1;
}
return false;
}

private String processGetLookupDataRegistry(String serviceName,
	String lookupKey) throws SQLException, IOException {

String lookupdata = "";
ResultSet oResultSet = null;
String sCommandText = "";
ArrayList<HashMap<String, Object>> alData = new ArrayList<HashMap<String, Object>>();
oConn = DriverManager.getConnection(configConnectionString);
DriverManager.setLoginTimeout(connectionTimeout);
try {

	sCommandText = " SELECT distinct g.lookupvalue FROM generallookup g ";
	sCommandText += " inner join generallookuptype t1 on g.generallookuptypeid=t1.generallookuptypeid ";

	sCommandText += " where t1.typename='" + serviceName + "' ";

	sCommandText += " and g.lookupkey='" + lookupKey + "' ";

	Statement oStatement = oConn.createStatement();
	oResultSet = oStatement.executeQuery(sCommandText);

	while (oResultSet.next()) {
		lookupdata = (String) oResultSet.getObject(1);
	}

} catch (SQLException ex) {
	throw ex;
} finally {
	if (oResultSet != null)
		oResultSet.close();
	if (oConn != null)
		oConn.close();
}

return lookupdata;
}

private String getStackTrace(Exception exception) {
String text = "";
Writer writer = null;
try {
	writer = new StringWriter();
	PrintWriter printWriter = new PrintWriter(writer);
	exception.printStackTrace(printWriter);
	text = writer.toString();
} catch (Exception e) {

} finally {
	if (writer != null) {
		try {
			writer.close();
		} catch (IOException e) {

		}
	}
}
return text;
}

public String prepareQuery(String input) {
input = input.replace("'null'", "null");
input = input.replace("'NULL'", "null");
input = input.replace("'Null'", "null");
return input;
}

public Integer getIntegerDB(Object obj) {
if (obj == null)
	return Types.NULL;
else if (obj.toString().equals("") || obj.toString().toLowerCase().equals("null"))
	return Types.NULL;
else
	return Integer.parseInt(obj.toString());
}

public boolean isBoolean(String str) {
try {
	
	if (str == null)
		return false;
	if (str.trim().length() == 0)
		return false;
	if (str.toLowerCase().equals("null") || str.toLowerCase().equals("'null'"))
		return false;
	if (str.toLowerCase().equals("0") || str.toLowerCase().equals("1"))
		return true;
	
	Boolean b = Boolean.parseBoolean(str);
	
} catch (Exception ex) {
	return false;
}
return true;
}

public Boolean getBoolean(Object obj) {
if (obj == null)
	return false;

if (obj.toString().equals("1") || obj.toString().toLowerCase().equals("true"))
	return true;
else if (obj.toString().equals("0") || obj.toString().toLowerCase().equals("false"))
	return false;
else if (obj.toString().trim().equals(""))
	return false;
else if (obj.toString().toLowerCase().trim().equals("null"))
	return false;
else if (obj.toString().toLowerCase().trim().equals("'null'"))
	return false;
else
	return false;
}

}
