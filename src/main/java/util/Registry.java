package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.ServletContext;

import model.ServletContextMock;
import util.DB;

public class Registry {
	
	private String lookupKey1 = "ConnectionStrings";
	private String lookupKey2 = "AOV3";
	

	
	private DB db = null;
	ServletContextMock app = null;

//	public Registry(ServletContextMock app2) throws Exception {
//		db = new DB(lookupKey1, lookupKey2);
//	}

	public Registry(ServletContextMock application) throws Exception {
		db = new DB(lookupKey1, lookupKey2, application);
		app = application;
	}

	public Registry(ServletContextMock application, String pathDBProperties)
			throws Exception {
		db = new DB(lookupKey1, lookupKey2, application, pathDBProperties);
		app = application;
	}

	public Registry() {
		// TODO Auto-generated constructor stub
	}

	public String getLookupDataRegistry(String serviceName, String lookupKey)
			throws Exception {
		return processGetLookupDataRegistry(null, serviceName, lookupKey);
	}

	public String getLookupDataRegistry(String parentServiceName,
			String serviceName, String lookupKey) throws Exception {
		return processGetLookupDataRegistry(parentServiceName, serviceName,
				lookupKey);
	}

	public String processGetLookupDataRegistry(String parentServiceName,
			String serviceName, String lookupKey) throws Exception {
		String cacheName = checkEmpty(parentServiceName).toLowerCase() + "_"
				+ checkEmpty(serviceName).toLowerCase() + "_"
				+ checkEmpty(lookupKey).toLowerCase();
		cacheName = cacheName.replace(" ", "").trim();
		if (app != null && app.getAttribute(cacheName) != null)
			return app.getAttribute(cacheName).toString();
		else {

		}
		PreparedStatement oStatement = null;
		String lookupdata = "";
		ResultSet oResultSet = null;
		String sCommandText = "";
		//System.out.println("db.connectionString=" + db.connectionString);
		//System.out.println("db.driver=" + db.driver);
		Connection oConn = DriverManager.getConnection(db.connectionString);
		DriverManager.setLoginTimeout(db.connectionTimeout);

		try {

			sCommandText = " SELECT distinct g.lookupvalue FROM generallookup g ";
			sCommandText += " inner join generallookuptype t1 on g.generallookuptypeid=t1.generallookuptypeid ";
			if (!IsEmpty(parentServiceName))
				sCommandText += " inner join generallookuptype t2 on t2.typeid=t1.typesubid ";
			sCommandText += " where t1.typename=? ";
			if (!IsEmpty(parentServiceName))
				sCommandText += " and t2.typename=? ";
			sCommandText += " and g.lookupkey=? ";
			
			System.out.println("sCommandText: "+sCommandText);
			System.out.println("parentServiceName: "+parentServiceName);
			System.out.println("serviceName: "+serviceName);
			System.out.println("lookupKey: "+lookupKey);
			

			sCommandText = db.prepareQuery(sCommandText);
			oStatement = oConn.prepareStatement(sCommandText);
			int p = 1;
			oStatement.setString(p++, serviceName);
			if (!IsEmpty(parentServiceName))
				oStatement.setString(p++, parentServiceName);

			oStatement.setString(p++, lookupKey);
			oResultSet = oStatement.executeQuery();

			while (oResultSet.next()) {
				lookupdata = (String) oResultSet.getObject(1);
			}
			
			if(oStatement!=null)
				oStatement.close();
			if (oResultSet != null)
				oResultSet.close();
			if (oConn != null)
				oConn.close();
				

		} catch (Exception e) {
			e = db.writeError(e);
			throw e;
		} finally {
			if (oResultSet != null)
				oResultSet.close();
			if (oStatement != null)
				oStatement.close();
			if (oConn != null)
				oConn.close();
		}
		if (app != null && app.getAttribute(cacheName) == null)
			app.setAttribute(cacheName, lookupdata);
		return lookupdata;
	}

	public ArrayList<HashMap<String, Object>> getLookupDataRegistryList(
			String parentServiceName, String serviceName, String lookupKey)
			throws Exception {
		ArrayList<HashMap<String, Object>> alData = new ArrayList<HashMap<String, Object>>();
		ResultSet oResultSet = null;
		String sCommandText = "";
		Connection oConn = DriverManager.getConnection(db.connectionString);
		DriverManager.setLoginTimeout(db.connectionTimeout);
		PreparedStatement oStatement = null;
		try {

			sCommandText = " SELECT distinct g.* FROM generallookup g ";
			sCommandText += " inner join generallookuptype t1 on g.generallookuptypeid=t1.generallookuptypeid ";
			if (!IsEmpty(parentServiceName))
				sCommandText += " inner join generallookuptype t2 on t2.typeid=t1.typesubid ";
			sCommandText += " where t1.typename=? ";
			if (!IsEmpty(parentServiceName))
				sCommandText += " and t2.typename=? ";
			sCommandText += " and g.lookupkey=? ";

			sCommandText = db.prepareQuery(sCommandText);
			oStatement = oConn.prepareStatement(sCommandText);
			int p = 1;
			oStatement.setString(p++, serviceName);
			if (!IsEmpty(parentServiceName))
				oStatement.setString(p++, parentServiceName);

			oStatement.setString(p++, lookupKey);
			oResultSet = oStatement.executeQuery();

			ResultSetMetaData oMeta = oResultSet.getMetaData();
			int columns = oMeta.getColumnCount();

			while (oResultSet.next()) {
				HashMap<String, Object> oRow = new HashMap<String, Object>();

				for (int i = 1; i <= columns; i++) {
					oRow.put(oMeta.getColumnLabel(i).toLowerCase(),
							oResultSet.getObject(i));
				}
				alData.add(oRow);
			}

			if(oStatement!=null)
				oStatement.close();
			if (oResultSet != null)
				oResultSet.close();
			if (oConn != null)
				oConn.close();
		} catch (Exception e) {
			e = db.writeError(e);
			throw e;
		} finally {
			if (oResultSet != null)
				oResultSet.close();
			if (oStatement != null)
				oStatement.close();
			if (oConn != null)
				oConn.close();
		}
		return alData;
	}

	public ArrayList<HashMap<String, Object>> getLookupDataRegistryList(
			String id, String typeId, String lookupKey, String lookupValue,
			String lookupText) throws Exception {
		ArrayList<HashMap<String, Object>> alData = new ArrayList<HashMap<String, Object>>();
		ResultSet oResultSet = null;
		String sCommandText = "";
		Connection oConn = DriverManager.getConnection(db.connectionString);
		DriverManager.setLoginTimeout(db.connectionTimeout);
		try {

			sCommandText = " SELECT distinct g.id, g.lookupkey, g.lookupvalue, g.lookuptext ";
			sCommandText += " , g.createddate, g.updateddate ";
			sCommandText += " , t1.generallookuptypeid lookuptypeid, t1.typename lookuptypename ";
			sCommandText += " , t2.generallookuptypeid lookuptypesubid, t2.typename lookuptypesubname ";
			sCommandText += " FROM generallookup g ";
			sCommandText += " inner join generallookuptype t1 on g.generallookuptypeid=t1.generallookuptypeid ";
			sCommandText += " left outer join generallookuptype t2 on t2.typeid=t1.typesubid";
			sCommandText += " WHERE 1=1";

			if (!IsEmpty(id))
				sCommandText += " and g.id=? ";
			if (!IsEmpty(typeId))
				sCommandText += " and (t1.generallookuptypeid=?  or t2.generallookuptypeid=?)";
			if (!IsEmpty(lookupKey))
				sCommandText += " and g.lookupkey like ? ";
			if (!IsEmpty(lookupValue))
				sCommandText += " and g.lookupvalue like ? ";
			if (!IsEmpty(lookupText))
				sCommandText += " and g.lookuptext like ? ";

			sCommandText += " order by t2.typename, t1.typename, g.lookupkey";

			sCommandText = db.prepareQuery(sCommandText);
			PreparedStatement oStatement = oConn.prepareStatement(sCommandText);
			int p = 1;
			if (!IsEmpty(id))
				oStatement.setString(p++, id);
			if (!IsEmpty(typeId))
				oStatement.setString(p++, typeId);
			if (!IsEmpty(typeId))
				oStatement.setString(p++, typeId);
			if (!IsEmpty(lookupKey))
				oStatement.setString(p++, "%" + lookupKey + "%");
			if (!IsEmpty(lookupValue))
				oStatement.setString(p++, "%" + lookupValue + "%");

			if (!IsEmpty(lookupText))
				oStatement.setString(p++, "%" + lookupText + "%");
			 
			oResultSet = oStatement.executeQuery();

			ResultSetMetaData oMeta = oResultSet.getMetaData();
			int columns = oMeta.getColumnCount();

			while (oResultSet.next()) {
				HashMap<String, Object> oRow = new HashMap<String, Object>();

				for (int i = 1; i <= columns; i++) {
					oRow.put(oMeta.getColumnLabel(i).toLowerCase(),
							oResultSet.getObject(i));
				}
				alData.add(oRow);
			}
			
			if(oStatement!=null)
				oStatement.close();
			if (oResultSet != null)
				oResultSet.close();
			if (oConn != null)
				oConn.close();

		} catch (Exception e) {
			e = db.writeError(e);
			throw e;
		} finally {
			if (oResultSet != null)
				oResultSet.close();
			if (oConn != null)
				oConn.close();
		}
		return alData;
	}

	public ArrayList<HashMap<String, Object>> getLookupDataRegistryTypeList()
			throws Exception {
		ArrayList<HashMap<String, Object>> alData = new ArrayList<HashMap<String, Object>>();
		ResultSet oResultSet = null;
		String sCommandText = "";
		Connection oConn = DriverManager.getConnection(db.connectionString);
		DriverManager.setLoginTimeout(db.connectionTimeout);
		try {

			sCommandText = " SELECT distinct t.* FROM generallookuptype t ";
			sCommandText += " order by t.typename";

			Statement oStatement = oConn.createStatement();
			oResultSet = oStatement.executeQuery(sCommandText);

			ResultSetMetaData oMeta = oResultSet.getMetaData();
			int columns = oMeta.getColumnCount();

			while (oResultSet.next()) {
				HashMap<String, Object> oRow = new HashMap<String, Object>();

				for (int i = 1; i <= columns; i++) {
					oRow.put(oMeta.getColumnLabel(i).toLowerCase(),
							oResultSet.getObject(i));
				}
				alData.add(oRow);
			}
			
			if(oStatement!=null)
				oStatement.close();
			if (oResultSet != null)
				oResultSet.close();
			if (oConn != null)
				oConn.close();

		} catch (Exception e) {
			e = db.writeError(e);
			throw e;
		} finally {
			if (oResultSet != null)
				oResultSet.close();
			if (oConn != null)
				oConn.close();
		}
		return alData;
	}

	public boolean IsEmpty(Object object) {
		if (object == null)
			return true;

		if (object.toString().trim().length() == 0)
			return true;

		return false;
	}

	public void updateLookupDataRegistry(String id, String generallookuptypeid,
			String lookupid, String lookupkey, String lookupvalue,
			String lookuptext, String lookupint, String lookupdouble,
			String updatedby) throws Exception {
		Class.forName(db.driver).newInstance();

		Connection con = null;
		PreparedStatement stmt = null;

		try {
			con = DriverManager.getConnection(db.connectionString);

			String sCommandText = "";
			sCommandText = "update generallookup ";
			sCommandText += " set updateddate=now() ";
			sCommandText += ", updatedby=? ";
			sCommandText += ", lookupvalue=? ";

			if (!IsEmpty(generallookuptypeid))
				sCommandText += ", generallookuptypeid=? ";

			if (!IsEmpty(lookupid))
				sCommandText += ", lookupid=? ";

			if (!IsEmpty(lookupkey))
				sCommandText += ", lookupkey=? ";

			if (!IsEmpty(lookuptext))
				sCommandText += ", lookuptext=? ";

			if (!IsEmpty(lookupint))
				sCommandText += ", lookupint=? ";

			if (!IsEmpty(lookupdouble))
				sCommandText += ", lookupdouble=? ";

			sCommandText += " where id=? ";

			sCommandText = db.prepareQuery(sCommandText);

			stmt = con.prepareStatement(sCommandText);
			int p = 1;
			stmt.setString(p++, updatedby);
			stmt.setString(p++, lookupvalue);
			if (!IsEmpty(generallookuptypeid))
				stmt.setString(p++, generallookuptypeid);
			if (!IsEmpty(lookupid))
				stmt.setString(p++, lookupid);
			if (!IsEmpty(lookupkey))
				stmt.setString(p++, lookupkey);
			if (!IsEmpty(lookuptext))
				stmt.setString(p++, lookuptext);
			if (!IsEmpty(lookupint))
				stmt.setString(p++, lookupint);
			if (!IsEmpty(lookupdouble))
				stmt.setString(p++, lookupdouble);

			stmt.setString(p++, id);

			stmt.executeUpdate();

			
			if(stmt!=null)
				stmt.close();
			if (con != null)
				con.close();
			 
		} catch (Exception e) {
			e = db.writeError(e);
			throw e;
		} finally {
			if (stmt != null)
				stmt.close();
			if (con != null)
				con.close();
		}
	}

	public String checkEmpty(Object object) {
		if (object == null)
			return "";

		return object.toString();
	}

	public String getStringQuery(String input) {
		return input.replaceAll("'", "''");
	}

}
