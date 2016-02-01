#!/bin/bash

cd $(dirname $0)

./playgame.py --cutoff_turn 1000 --verbose -SoER --nolaunch \
    --player_seed $RANDOM --end_wait=0.25 --verbose --log_dir game_logs \
    --turns 1000 --map_file "$@"
