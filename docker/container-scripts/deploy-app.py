import os

print '********************'
print 'Configure Application'

instance_type = os.environ.get('INSTANCE_TYPE')
admin_name = os.environ.get('ADMIN_NAME')
domainhome = os.environ.get('DOMAIN_HOME')

print('Load properties @Environment');
print('instance_type  : [%s]' % instance_type);
print('admin_name     : [%s]' % admin_name);
print('domainhome     : [%s]\n' % domainhome);

print('Load properties @%s.properties' % instance_type);
print('appname  : [%s]' % appname);
print('apppkg   : [%s]\n' % apppkg);


readDomain(domainhome)

app = create(appname, 'AppDeployment')
app.setSourcePath( '/u01/oracle/' + apppkg)
app.setStagingMode('nostage')

assign('AppDeployment', appname, 'Target', admin_name)

updateDomain()
closeDomain()
exit()
