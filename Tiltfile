# Build
custom_build(
    # name of the container image
    ref = 'catalog-service',
    # command to build the container image
    command = './gradlew bootBuildImage --imageName $EXPECTED_REF',
    # Files to watch that trigger a new build
    deps = ['build.gradle', 'src']
)

# Deploy
k8s_yaml([kustomize('k8s')])

# Manage
k8s_resource('catalog-service', port_forwards=['9001'])
