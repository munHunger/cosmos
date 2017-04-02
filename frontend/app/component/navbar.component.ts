import { Component } from '@angular/core';
import { StuckItemsService } from '../service/stuck-items.service';
import { VideoItem } from '../model/video-item.model';

@Component({
    selector: 'navbar',
    moduleId: module.id,
    providers: [StuckItemsService],
    templateUrl: 'navbar.component.html'
})
export class Navbar {
  hidden: boolean = false;
  stuckItems: VideoItem[];
  constructor(stuckItemService: StuckItemsService){
    this.stuckItems = stuckItemService.getItems();
  }
}
