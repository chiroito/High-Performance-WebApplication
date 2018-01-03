import os

# https://docs.oracle.com/middleware/1221/wls/CLUST/coherence.htm
print '********************'
print 'Configure Coherence Cluster'

instance_type = os.environ.get('INSTANCE_TYPE')
admin_name = os.environ.get('ADMIN_NAME')
domainhome = os.environ.get('DOMAIN_HOME')

print('Load properties @Environment');
print('instance_type  : [%s]' % instance_type);
print('admin_name     : [%s]' % admin_name);
print('domainhome     : [%s]\n' % domainhome);

print('Load properties @%s.properties' % instance_type);
print('local_storage_enabled  : [%s]\n' % local_storage_enabled);

cluster_name = 'myCoherenceCluster'
cluster_home = '/CoherenceClusterSystemResource/' + cluster_name + '/CoherenceResource/' + cluster_name

readDomain(domainhome)

print 'Create Coherence Cluster'
create(cluster_name, 'CoherenceClusterSystemResource')

cd(cluster_home + '/CoherenceClusterParams/NO_NAME_0')
set('ClusteringMode', 'unicast')
set('SecurityFrameworkEnabled', 'false')
set('ClusterListenPort', 7574)
create('wka_config', 'CoherenceClusterWellKnownAddresses')

cd('CoherenceClusterWellKnownAddresses/NO_NAME_0')
create('WKA', 'CoherenceClusterWellKnownAddress')

cd('CoherenceClusterWellKnownAddress/WKA')
set('ListenAddress', 'coherence1')

print 'Configure ' + admin_name
cd('/Server/' + admin_name)
set('CoherenceClusterSystemResource', cluster_name)
create('member_config', 'CoherenceMemberConfig')

cd('/Server/' + admin_name + '/CoherenceMemberConfig/member_config')
set('LocalStorageEnabled', local_storage_enabled)
set('SiteName', 'Tokyo')
set('UnicastListenPort', 8088)

updateDomain()
closeDomain()
exit()
