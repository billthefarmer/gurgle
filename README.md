# ![Logo](src/main/res/drawable-hdpi/ic_launcher.png) Gurgle [![.github/workflows/build.yml](https://github.com/billthefarmer/gurgle/actions/workflows/build.yml/badge.svg)](https://github.com/billthefarmer/gurgle/actions/workflows/build.yml)

[<img src="https://fdroid.gitlab.io/artwork/badge/get-it-on.svg" alt="Get it on F-Droid" height="80">](https://f-droid.org/packages/org.billthefarmer.gurgle/)

Fairly simple android word game.

![Gurgle](https://github.com/billthefarmer/billthefarmer.github.io/raw/master/images/Gurgle.png) ![Help](https://github.com/billthefarmer/billthefarmer.github.io/raw/master/images/Gurgle-help.png)

 * Multiple coloured themes
 * Multiple languages
 * Look up word meaning

## Guess those words
Tap letters on the keyboard to enter a guess. Use the **Back** key to
backspace. When you have finished a guess, tap the **Enter** key. If
you have guessed the word correctly, a fanfare will play and a short
message will appear. Tap the word for it's definition.

## Display colours
Letters which are in the word but in the wrong place will turn
yellow. If they are in the right place they will turn green. If they
are not in the word they will turn grey. Keep trying until you have
guessed the word. The display will scroll up if you run out of
guesses. The colours may be changed by selecting **Highlight colours**
in the menu.

## Lock letters
Long press on a letter in the current guess to lock it, which will
turn it grey. Long press again to remove the lock. Locked letters will
be skipped when entering and deleting letters. Long presses on letters
not part of the current guess will pop up a menu as below.

## Add accents
Long press on a letter in a completed guess. This will show a pop-up
menu of unaccented and accented letters. Pick the required unaccented
or accented letter.

## Swap enter/backspace
Long press on the enter or backspace key to swap the keys.

## Meaning
Tap any word to display the meaning of that word. Add any required
accents as above.

## Refresh
Tap the **Refresh** button in the toolbar to clear the display and
guess another word. Words are chosen at random from a list, and are
checked that they haven't been used recently.

## Share
### Image
Share an image of the current display.

### Text
Share the letters and colours of finished guesses. Select the
[WordleSolver](https://github.com/billthefarmer/wordlesolver) app to
pick the next guess.

### Code
Display an alphanumeric code and a QR code which represents a new
sequence of words to guess. This code may be shared with another copy
of Gurgle on another device to set the same sequence of words. Tapping
the QR code button on the dialog shares the QR code image. Tapping the
code button shares the alphanumeric code.

## Themes
Select coloured theme from the **Theme** button in the toolbar.

## Options
 * **Confetti** &ndash; Select confetti display on guessing word.
 * **Fanfare** &ndash; Select fanfare on guessing word.
 * **Default word** &ndash; Select to enable default word. The first
   word entered will become the default word entered at the beginning
   of each game. Reset to clear word.

## Dictionary
Select the dictionary to look up a word meaning. Wiktionary (online)
is built in, [Aard2](https://f-droid.org/en/packages/itkach.aard2/)
and
[Quickdic](https://f-droid.org/en/packages/de.reimardoeffinger.quickdic/)
are apps which can be installed from [F-Droid](https://f-droid.org/).

## Language
Select **Language** from the menu.

## Get code
### Code
Enter the code shared by another copy of Gurgle to set the same
sequence of words. If a game is in progress tap the refresh button to
start the new sequence. You may also make up your own code. It doesn't
need the two equals symbols on the end ('='), but will fail with just
one.

### QR code
Opens the camera app on the phone to take a photo of a QR code. The
app will attempt to decode it. There are plenty of very good QR code
and barcode scanner apps that do a better job.

### Receiving a code
If the app is sent a code or a QR code image it will set a new
sequence of words if the code is decoded successfully. This does not
affect the current game. If a game is in progress tap the refresh
button to start the new sequence.

## Highlight colours
A dialog will pop up showing two guesses, one containing some letters
in the word in the wrong place and a correct guess, and three
spectrums. Change the colour of the letters in the wrong place by
tapping on the top spectrum, and the correct letters with the middle
spectrum. Change the shade of grey of the letters not in the word with
the bottom spectrum. The **Reset** button will restore the default
colours, The **Cancel** button changes nothing, and the **OK** button
accepts the changes.

## Cheat
Use my [Crossword](https://github.com/billthefarmer/crossword) or
[WordleSolver](https://github.com/billthefarmer/wordlesolver) app to
cheat.
