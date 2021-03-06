SELECT /*+ PARALLEL(AUTO) */
  NULL AS SEQUENCE_CATALOG,
  SEQUENCE_OWNER AS SEQUENCE_SCHEMA,
  SEQUENCE_NAME AS SEQUENCE_NAME,
  INCREMENT_BY AS "INCREMENT",
  MIN_VALUE AS MINIMUM_VALUE,
  MAX_VALUE AS MAXIMUM_VALUE,
  CASE WHEN CYCLE_FLAG = 'Y' THEN 'YES' ELSE 'NO' END AS CYCLE_OPTION,
  ORDER_FLAG,
  CACHE_SIZE,
  LAST_NUMBER
FROM
  ALL_SEQUENCES
WHERE
  SEQUENCE_OWNER NOT IN ('CTXSYS', 'DBSNMP', 'DMSYS', 'MDDATA', 'MDSYS', 'OLAPSYS', 'ORDPLUGINS', 'ORDSYS', 'OUTLN', 'SI_INFORMTN_SCHEMA', 'SYS', 'SYSMAN', 'SYSTEM', 'XDB')
  AND SEQUENCE_OWNER NOT LIKE 'APEX%'
ORDER BY
  SEQUENCE_OWNER,
  SEQUENCE_NAME
