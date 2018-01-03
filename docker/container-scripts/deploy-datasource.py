import java.lang
import os

print '********************'
print 'Configure Data Source'

instance_type = os.environ.get('INSTANCE_TYPE')
admin_name = os.environ.get('ADMIN_NAME')
domainhome = os.environ.get('DOMAIN_HOME')
dsjndiname='jdbc/TechDeepDive'

print('Load properties @Environment');
print('instance_type  : [%s]' % instance_type);
print('admin_name     : [%s]' % admin_name);
print('domainhome     : [%s]\n' % domainhome);

print('Load properties @datasource.properties');
print('dsjndiname  : [%s]' % dsjndiname);
print('dsdriver    : [%s]' % dsdriver);
print('dsname      : [%s]' % dsname);
print('dsurl       : [%s]' % dsurl);
print('dsdbname    : [%s]' % dsdbname);
print('dsusername  : [%s]' % dsusername);
print('dspassword  : [%s]' % dspassword);
print('dstestquery : [%s]\n' % dstestquery);

print('Load properties @%s.properties' % instance_type);
print('db_connection_capacity  : [%s]\n' % db_connection_capacity);

dshome = '/JDBCSystemResource/' + dsname + '/JdbcResource/' + dsname

readDomain(domainhome)

print 'Create Data Source'

create(dsname, 'JDBCSystemResource')

cd(dshome)
cmo.setName(dsname)
create('myJdbcDataSourceParams', 'JDBCDataSourceParams')

cd('JDBCDataSourceParams/NO_NAME_0')
set('JNDIName', java.lang.String(dsjndiname))
set('GlobalTransactionsProtocol', java.lang.String('None'))

cd(dshome)
create('myJdbcDriverParams', 'JDBCDriverParams')

cd('JDBCDriverParams/NO_NAME_0')
set('DriverName', dsdriver)
set('URL', dsurl)
set('PasswordEncrypted', dspassword)
set('UseXADataSourceInterface', 'false')

print 'Create JDBCDriverParams Properties'
create('myProperties', 'Properties')

cd('Properties/NO_NAME_0')
create('user', 'Property')
create('databaseName', 'Property')

cd('Property/user')
set('Value', dsusername)

cd('../databaseName')
set('Value', dsdbname)

print 'Create JDBCConnectionPoolParams'
cd(dshome)
create('myJdbcConnectionPoolParams', 'JDBCConnectionPoolParams')

cd('JDBCConnectionPoolParams/NO_NAME_0')
set('TestTableName', dstestquery)
cmo.setInitialCapacity(int(db_connection_capacity))
cmo.setMinCapacity(int(db_connection_capacity))
cmo.setMaxCapacity(int(db_connection_capacity))

print 'Assign DataSource to AdminServer'
assign('JDBCSystemResource', dsname, 'Target', admin_name)

updateDomain()
closeDomain()
exit()
