import {Component, OnDestroy, OnInit} from '@angular/core';
import {Title} from '@angular/platform-browser';
import {Router} from '@angular/router';
import {Gif} from './gif';
import {SpeechToGifService} from './speech-to-gif.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
})
export class AppComponent implements OnInit, OnDestroy {


  constructor(private speechToGifService: SpeechToGifService,
              private title: Title,
              private router: Router) {
    this.speechToGifService.init();
    this.startListening();
  }

  ngOnInit() {
    this.speechToGifService.gifForSpeech$.subscribe((gif: Gif) => {
      this.title.setTitle(`${gif.query} | ${gif.fullText}`);
      this.router.navigate([''], {queryParams: {...gif}}).then();
    });
  }

  ngOnDestroy() {
    this.speechToGifService.abort();
  }

  startListening(): void {
    this.speechToGifService.startListening();
  }


}
