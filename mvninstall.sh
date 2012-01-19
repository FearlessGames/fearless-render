function install {
  FILE=$1
  ARTIFACTID=$2
  VERSION=$3

  mvn install:install-file \
  -Dfile=$FILE \
  -DgroupId=local \
  -DartifactId=$ARTIFACTID \
  -Dversion=$VERSION \
  -Dpackaging=jar \
  -DgeneratePom=true
  
}

install lib/slick-util.jar slick-util 0.0.1

