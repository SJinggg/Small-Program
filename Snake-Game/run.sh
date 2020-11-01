if [[ -f "target/SnakeGame.jar" ]]; then
  java -jar target/SnakeGame.jar
else
  echo "Couldn't find game executable, perhaps you forget to build?"
  exit 1
fi
exit 0