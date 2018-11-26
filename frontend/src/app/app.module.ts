import {HttpClientModule} from '@angular/common/http';
import {NgModule} from '@angular/core';
import {MatButtonModule, MatIconModule, MatRippleModule} from '@angular/material';
import {BrowserModule} from '@angular/platform-browser';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {RouterModule, Routes} from '@angular/router';
import {ServiceWorkerModule} from '@angular/service-worker';

import {AppComponent} from './app.component';
import {GifComponent} from './gif/gif.component';


const routes: Routes = [{
  path: '',
  component: GifComponent,
}];

@NgModule({
  declarations: [
    AppComponent,
    GifComponent,
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    BrowserAnimationsModule,
    MatIconModule,
    MatButtonModule,
    MatRippleModule,
    RouterModule.forRoot(routes, {useHash: true}),
    ServiceWorkerModule.register('ngsw-worker.js', {enabled: false}),
  ],
  bootstrap: [AppComponent],
})

export class AppModule {
}
