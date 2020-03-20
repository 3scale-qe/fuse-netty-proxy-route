# fuse-netty-proxy-route

## Deploy on OpenShift
Log in to the OpenShift:

    $ oc login -u <developer> -p <developer> https://OPENSHIFT_IP_ADDR:8443
 
Switch to OpenShift project:

    $ oc project <project>
  
Finally, build and deploy the project:

    $ mvn fabric8:deploy -Popenshift
