java -classpath $(echo ../../_schemacrawler/lib/*.jar | tr ' ' ':') schemacrawler.Main -server=hsqldb -database=schemacrawler -user=sa -password= -infolevel=standard -command thymeleaf -infolevel=maximum -sorttables=false -outputformat $1