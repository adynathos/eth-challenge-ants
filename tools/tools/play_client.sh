#!/bin/bash
GAME_ID_FILE=last_game_id
DEFAULTMAP=maps/maze/maze_04p_01.map
MAP=$DEFAULTMAP

# numbers N from 01 to 56
# MAP=maps/maze/maze_p04_N.map
#MAP=maps/maze/maze_p04_37.map

# numbers N from 04 to 26
# MAP=maps/cell_maze/cell_maze_p04_N.map
#MAP=maps/cell_maze/cell_maze_p04_17.map

# numbers N from 01 to 10
# MAP=maps/random_walk/random_walk_p04_N.map
MAP=maps/random_walk/random_walk_p04_05.map

#MAP=maps/maze/maze_p04_27.map

N=4

#BOT[1]="python sample_bots/python/HunterBot.py"
#BOT[2]="python sample_bots/python/GreedyBot.py"
#BOT[3]="python sample_bots/python/LeftyBot.py"
#BOT[4]="python sample_bots/python/RandomBot.py"
#BOT[4]="python bernhard/BBot.pyo"

BOT[1]="python smart/SmartBot.py"
BOT[2]="python noidea/NoIdeaBot.py"
BOT[3]="python awesome2/AwesomeBot.py"
BOT[4]="python rms/RMS.py"


test -f $GAME_ID_FILE || echo 0 >$GAME_ID_FILE

GAME_ID=$(cat $GAME_ID_FILE)
let GAME_ID++
echo $GAME_ID >$GAME_ID_FILE

echo ID for this game: $GAME_ID

ORDER=$(for ((i=1; i<=$N; i++)) ; do echo $i ; done | sort -R)
echo ORDER $ORDER

BOTCMDS=""
CNT=0
for i in $ORDER ; do
    let CNT++
    B=${BOT[$i]}
    echo Player $CNT: $B
    BOTCMDS="$BOTCMDS '$B'"
done

scp -q play_server.sh ants@dax:tools-dryrun/

ssh ants@dax tools-dryrun/play_server.sh $MAP \
    --game $GAME_ID \
    $BOTCMDS |
    java -jar visualizer.jar

