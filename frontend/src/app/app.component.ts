import {ChangeDetectionStrategy, ChangeDetectorRef, Component, NgZone, OnDestroy, OnInit} from '@angular/core';
import {SpeechService} from './speech.service';
import {Gif} from './gif';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class AppComponent implements OnInit, OnDestroy {
  gif: Gif;

  constructor(private speechService: SpeechService,
              private ngZone: NgZone,
              private cdRef: ChangeDetectorRef) {
    this.speechService.init();
    this.startListening();
  }

  ngOnInit() {

    this.speechService._ws$.asObservable().subscribe((gif: Gif) => {
      this.gif = gif;
      this.cdRef.detectChanges();
    });
  }

  ngOnDestroy() {
    this.speechService.abort();
  }

  startListening(): void {
    this.speechService.startListening();
  }

}
