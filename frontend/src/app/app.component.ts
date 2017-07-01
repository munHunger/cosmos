import { Component } from '@angular/core';

@Component({
  selector: 'cosmos',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})

export class AppComponent  {
  name = 'Angular';

  visible = false;
  getSideBarDisplayStyle(): any {
    if(this.visible) {
      return "block";
    }
    else {
      return "none";
    }
  }
  getViewWindowStyle(): any {
    if(this.visible) {
      return '200px';
    }
    else {
      return '0';
    }
  }
}
