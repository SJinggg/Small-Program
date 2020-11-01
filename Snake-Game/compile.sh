echo "Trying to compile resources..."
if [[ -d "target/bin" ]]; then
  rm -r target
fi
mkdir -p target/bin
if javac -d ./target/bin -cp ./src ./src/com/me/snake/Snake.java ; then
  echo "Compiled success!"
else
  echo "Failed to compile!"
  exit 1
fi

echo "Packaging binaries..."
cp -r src/resources target/bin/
if jar cfm target/SnakeGame.jar manifest.txt -C target/bin . ; then
  echo "Package success!"
else
  echo "Package failed!"
  exit 1
fi

exit 0