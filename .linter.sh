#!/bin/bash
cd /home/kavia/workspace/code-generation/tictactoe-38413-9023d80e/tic_tac_toe
./gradlew lint
LINT_EXIT_CODE=$?
if [ $LINT_EXIT_CODE -ne 0 ]; then
   exit 1
fi

