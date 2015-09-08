# Snooker counter Java lib #

**used in android app :** <https://play.google.com/store/apps/details?id=com.advanetsystems.snooker&hl=en><br>
**and a web page:** <http://snookerscore.com><br>

### What is it? ###
This is a Java library to keep track of snooker matches. It can be used in snooker games, snooker tracking apps and live scoring apps.

Main features:

- Keeps track of a whole match
- Keep the game in line with the rules - handles al the snooker rules (free ball, multiple reds, etc...)
- Provide next legal pots.
- Counts breaks
- Provides points behind/on the table
- ability to undo every event (even between frames)
- ability to interact with the game - listen to game events
- put custom model data which the engine will be operating on

### How to use ###

Simple usage

```
        Match match = new MatchBuilder().frames(3).player1("Adam").player2("John").build();
        match.startNewFrame();
        match.registerPot(Ball.RED, Player.ONE);
        match.registerPot(Ball.BLACK, Player.ONE);
        match.registerPot(Ball.RED, Player.ONE);
        System.out.println("Current break: " + match.getCurrentFrame().getCurrentBreak(Player.ONE));

        match.registerFaul(4, Player.ONE, 0); // 4 points, player one, 0 reds gone of the table
        match.registerPot(Ball.RED, Player.TWO);
        match.registerPot(Ball.BLUE, Player.TWO);
        match.registerPot(Ball.RED, Player.TWO);
        match.registerPot(Ball.YELLOW, Player.TWO);
        match.registerPot(Ball.RED, Player.TWO);
        match.registerMiss();

        match.registerPot(Ball.RED, Player.TWO);
        match.registerPot(Ball.BLACK, Player.TWO);
        match.registerPot(Ball.RED, Player.TWO);
        match.registerPot(Ball.BLACK, Player.TWO);
        match.registerMiss();

        System.out.println("Current break: " + match.getCurrentFrame().getCurrentBreak(Player.TWO));
        System.out.println("Current scores: " + match.getCurrentFrame().getResult(Player.ONE)+" : "+match.getCurrentFrame().getResult(Player.TWO));

        match.surrenderFrame(Player.ONE);
        System.out.println("Frame scores: " + match.getResult(Player.ONE) + " : " + match.getResult(Player.TWO));
```

Usage of all features you can find in jUnit tests.