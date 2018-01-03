import os

print '********************'
print 'Configure WebLogic Server'

instance_type = os.environ.get('INSTANCE_TYPE')
admin_name = os.environ.get('ADMIN_NAME')
domainhome = os.environ.get('DOMAIN_HOME')

print('Load properties @Environment');
print('instance_type  : [%s]' % instance_type);
print('admin_name     : [%s]' % admin_name);
print('domainhome     : [%s]\n' % domainhome);

print('Load properties @%s.properties' % instance_type);
print('load_balancer_name        : [%s]' % load_balancer_name);
print('weblogic_thread_pool_num  : [%s]\n' % weblogic_thread_pool_num);

readDomain(domainhome)

cd('/Server/' + admin_name)
cmo.setExternalDNSName(load_balancer_name)
cmo.setSelfTuningThreadPoolSizeMin(int(weblogic_thread_pool_num))
cmo.setSelfTuningThreadPoolSizeMax(int(weblogic_thread_pool_num))

updateDomain()
closeDomain()
exit()
