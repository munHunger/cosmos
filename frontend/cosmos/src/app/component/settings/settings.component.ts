import { Component, Input } from '@angular/core';
import { SettingsService } from '../../service/settings.service';
import { Setting } from '../../model/setting.model';

@Component({
  selector: 'settings',
  templateUrl: './settings.component.html',
  styleUrls: ['./settings.component.css']
})
export class SettingsComponent {

    private settings: Setting[];

    constructor(private settingsService: SettingsService)
    {
        this.settings = settingsService.getSettings();
    }
}