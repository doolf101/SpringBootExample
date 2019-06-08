package com.doolf101.app.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public abstract class SuperRepository {

	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	private static final String E              = " \n";
	private static final String T              = "\t";

	private static final String OPEN           = "(";
	private static final String CLOSE          = ")";
	private static final String COLON          = ":";
	private static final String COMMA          = ",";
	private static final String BLANK          = "";

	private static final String INSERT_INTO    = "INSERT INTO ";
	private static final String VALUES         = "VALUES";
	private static final String DELETE_FROM    = "DELETE FROM ";
	private static final String UPDATE_SQL     = "UPDATE ";
	private static final String SET            = "SET";

	private static final String WHERE          = "WHERE ";
	private static final String AND            = " AND ";
	private static final String IS             = " IS ";
	private static final String EQUAL          = " = ";

	// #############################################################################
	// MISC ########################################################################
	// #############################################################################

	@SafeVarargs
	private final MapSqlParameterSource setParameters(Map<String, Object>... maps) {
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		for(Map<String, Object> map:maps)
			if(map!=null)
				for(Map.Entry<String, Object> e:map.entrySet())
					parameters.addValue(e.getKey(), e.getValue());
		return parameters;
	}

	private void modify(String sql,MapSqlParameterSource parameters){
		System.out.println(sql);
		namedParameterJdbcTemplate.update(sql,parameters);
	}

	private String whereClause(Map<String, Object> keys) {
		if(keys==null || keys.isEmpty())
			return BLANK;
		return WHERE+E+pramsEqual(keys,AND);
	}

	private String pramsEqual(Map<String, Object> keys,String end) {
		StringBuilder sb = new StringBuilder();
		int i = 1;
		for(Map.Entry<String, Object> e:keys.entrySet())
			sb.append(T+e.getKey()+((e.getValue()==null)?IS:EQUAL)+COLON+e.getKey()+((i++==keys.keySet().size())?BLANK:end)+E);
		return sb.toString();
	}

	// #############################################################################
	// INSERT ######################################################################
	// #############################################################################

	/**
	 * Insert Map @prams into @tableName and return @tableName new @pkColumnName ID
	 * @param tableName The name of the table to insert the @prams into
	 * @param prams The key value pairs of the columns and values to insert
	 * @param pkColumnName the Primary Key column Name to return the value
	 * @return value of the new Primary Key
	 */
	protected Integer insert(String tableName,Map<String, Object> prams,String pkColumnName) {
		sanitizeParams(prams);
		return returnPrimaryKey(insertSQL(tableName, prams), setParameters(prams), pkColumnName);
	}

	/**
	 * Insert Map @prams into @tableName and
	 * @param tableName The name of the table to insert the @prams into
	 * @param prams The key value pairs of the columns and values to insert
	 */
	protected void insert(String tableName, Map<String, Object> prams) {
		sanitizeParams(prams);
		modify(insertSQL(tableName, prams), setParameters(prams));
	}

	private void sanitizeParams(Map<String, Object> prams) {
		for(Iterator<Object> iterator = prams.values().iterator(); iterator.hasNext();){
			Object obj = iterator.next();
			if(obj == null || obj.equals("null") || obj.equals("NULL"))
				iterator.remove();
		}
	}

	private String insertSQL(String tableName,Map<String, Object> prams) {
		StringBuilder sb = new StringBuilder(INSERT_INTO + tableName + OPEN + E);
		int i = 1;
		for (String pram : prams.keySet())
			sb.append(T + pram + ((i++ == prams.keySet().size()) ? BLANK : COMMA) + E);
		sb.append(CLOSE+VALUES+E+OPEN+E);
		i = 1;
		for(String pram:prams.keySet())
			sb.append(T+COLON+pram+((i++==prams.keySet().size())?BLANK:COMMA)+E);
		sb.append(CLOSE);
		return sb.toString();
	}

	private Integer returnPrimaryKey(String sql,MapSqlParameterSource prams,String pkColumnName) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		namedParameterJdbcTemplate.update(sql, prams, keyHolder, new String[] { pkColumnName });
		try {
			return keyHolder.getKey().intValue();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// #############################################################################
	// DELETE ######################################################################
	// #############################################################################

	/**
	 *
	 * @param tableName The name of the table the Delete is to be run on
	 * @param keys The columns and values to delete
	 */
	protected void delete(String tableName,Map<String, Object> keys) {
		modify(DELETE_FROM+tableName+E+whereClause(keys), setParameters(keys));
	}

	// #############################################################################
	// UPDATE ######################################################################
	// #############################################################################

	/**
	 *
	 * @param tableName The name of the table the Update is to be run on
	 * @param prams The columns and values to update with
	 * @param keys The columns and values to update on
	 */
	protected void update(String tableName,Map<String, Object> prams,Map<String, Object> keys) {
		modify(UPDATE_SQL+tableName+E+setClause(prams)+whereClause(keys),setParameters(prams,keys));
	}

	private String setClause(Map<String, Object> keys) {
		if(keys.size()==0)
			return BLANK;
		StringBuilder sb = new StringBuilder(SET+E);
		int i = 1;
		for(Map.Entry<String, Object> e:keys.entrySet())
			sb.append(T+e.getKey()+EQUAL+COLON+e.getKey()+((i++==keys.keySet().size())?BLANK:COMMA)+E);
		return sb.toString();
	}

	// #############################################################################
	// SELECT ######################################################################
	// #############################################################################

	/**
	 * Select query with Where statment is generated returning an Instance
	 * @param sql The SQL of the query
	 * @param pram The prams to be used on the query
	 * @param mappedClass The Java Class type which should be returned
	 * @return Single Instance of Result
	 */
	protected <E> E getObjectWhere(String sql,Map<String, Object> pram,Class<E> mappedClass) {
		return getObject(sql+E+whereClause(pram), pram, mappedClass);
	}

	/**
	 * Select query with Where statment is generated returning a List
	 * @param sql The SQL of the query
	 * @param pram The prams to be used on the query
	 * @param mappedClass The Java Class type which should be returned
	 * @return List of Results
	 */
	protected <E> List<E> getWhere(String sql,Map<String, Object> pram,Class<E> mappedClass) {
		return get(sql+E+whereClause(pram), pram, mappedClass);
	}

	/**
	 * Select query returning an Instance
	 * @param sql The SQL of the query
	 * @param pram The prams to be used on the query
	 * @param mappedClass The Java Class type which should be returned
	 * @return Single Instance of Result
	 */
	protected <E> E getObject(String sql,Map<String, Object> pram,Class<E> mappedClass){
		MapSqlParameterSource parameters = setParameters(pram);
		if(wrapper(mappedClass))
			return namedParameterJdbcTemplate.queryForObject(sql,parameters,mappedClass);
		return namedParameterJdbcTemplate.queryForObject(sql,parameters,new BeanPropertyRowMapper<>(mappedClass));
	}

	/**
	 * Select query returning a List
	 * @param sql The SQL of the query
	 * @param pram The prams to be used on the query
	 * @param mappedClass The Java Class type which should be returned
	 * @return List of Results
	 */
	protected <E> List<E> get(String sql,Map<String, Object> pram,Class<E> mappedClass){
		MapSqlParameterSource parameters = setParameters(pram);
		if(wrapper(mappedClass))
			return namedParameterJdbcTemplate.query(sql,parameters,new SingleColumnRowMapper<>(mappedClass));
		return namedParameterJdbcTemplate.query(sql,parameters,new BeanPropertyRowMapper<>(mappedClass));
	}

	private boolean wrapper(Class<?> clazz) {
		List<Object> classes = new ArrayList<>();
		classes.add(Boolean.class);
		classes.add(Short.class);
		classes.add(Integer.class);
		classes.add(Long.class);
		classes.add(Float.class);
		classes.add(Double.class);
		classes.add(String.class);
		classes.add(Character.class);
		classes.add(Byte.class);
		for(Object c:classes)
			try {
				if(((Class<?>)c).isAssignableFrom(clazz))
					return true;
			}catch (Exception e) {
				return false;
			}
		return false;
	}

}
