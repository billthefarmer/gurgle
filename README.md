# ![Logo](src/main/res/drawable-hdpi/ic_launcher.png) Gurgle [![.github/workflows/build.yml](https://github.com/billthefarmer/gurgle/actions/workflows/build.yml/badge.svg)](https://github.com/billthefarmer/gurgle/actions/workflows/build.yml)

[<img src="https://fdroid.gitlab.io/artwork/badge/get-it-on.svg"
     alt="Get it on F-Droid"
     height="80">](https://f-droid.org/packages/org.billthefarmer.gurgle/)

Fairly simple android word game.

![Gurgle](https://github.com/billthefarmer/billthefarmer.github.io/raw/master/images/Gurgle.png) ![Help](https://github.com/billthefarmer/billthefarmer.github.io/raw/master/images/Gurgle-help.png)

 * Multiple coloured themes
 * Multiple languages
 * Look up word meaning

## Guess those words
Tap letters on the keyboard to enter a guess. Use the **Back** key to
backspace. When you have finished a guess, tap the **Enter** key. If
you have guessed the word correctly, music will play and a short
message will appear. Tap the display to stop the music, or tap the
word for it's definition.

## Display colours
Letters which are in the word but in the wrong place will turn
yellow. If they are in the right place they will turn green. If they
are not in the word they will turn grey. Keep trying until you have
guessed the word. The display will start again from the top if you run
out of guesses. The colours may be changed by selecting **Highlight
colours** in the menu.

## Refresh
Tap the **Refresh** button in the toolbar to clear the display and
guess another word. Words are chosen at random from a list, and are
checked that they haven't been used recently.

## Meaning
Tap the word once it has been guessed to display the meaning.

## Share
### Image
Share an image of the current display.

### Code
Display an alphanumeric code and a QR code which represents a new
sequence of words to guess. This code may be shared with another copy
of Gurgle on another device to set the same sequence of words. Tapping
the QR code button on the dialog shares the QR code image. Tapping the
code button shares the alphanumeric code.

## Themes
Select coloured theme from the **Theme** button in the toolbar.

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
in the word in the wrong place and a correct guess, and two
spectrums. Change the colour of the letters in the wrong place by
tapping on the top spectrum, and the correct letters with the bottom
spectrum.  The **Reset** button will restore the default colours, The
**Cancel** button changes nothing, and the **OK** button accepts the
changes.

## Cheat
Use my [Crossword](https://github.com/billthefarmer/crossword) or
[WordleSolver](https://github.com/billthefarmer/wordlesolver) app to
cheat.
