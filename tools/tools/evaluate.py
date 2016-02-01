#!/usr/bin/env python

import sys
import json
from collections import defaultdict

import numpy

class Player(object):
    def __init__ (self):
        self.rank = list()

    def add_rank (self, rank):
        self.rank.append (rank)

    def get_median (self):
        return numpy.median (self.socre)

    def get_mean (self):
        return float(sum(self.rank)) / float(len(self.rank))

    def num_games (self):
        return len(self.rank)

class Stats(object):
    def __init__ (self):
        self.players = defaultdict(Player)

    def add_ranks (self, playernames, ranks):
        assert len(playernames) == len(ranks)
        assert len(self.players) == 0 or len(playernames) == len(self.players), "%d %d %d" % (len(self.players), len(playernames), len(ranks))
        for playername, rank in zip (playernames, ranks):
            self.players[playername].add_rank (rank)

    def report_ranking (self):
        rankings = [ (player.get_mean(), pname) for pname, player in self.players.iteritems() ]
        rankings.sort()
        return rankings

    def assert_consistency (self):
        assert len (set (p.num_games() for p in self.players.itervalues())) == 1, "Some players left out ..."


def main ():

    stats = Stats()

    for fname in sys.argv[1:]:
        with open (fname) as f:
            j = json.load (f)
        stats.add_ranks (j['playernames'], j['rank'])

    stats.assert_consistency()

    rankings = stats.report_ranking ()
    print "\n".join ("%s %s" % r for r in rankings)


if __name__ == "__main__":
    main()
