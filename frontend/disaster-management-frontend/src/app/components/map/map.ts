import { Component, OnInit, AfterViewInit, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import * as L from 'leaflet';

@Component({
  selector: 'app-map',
  imports: [CommonModule],
  templateUrl: './map.html',
  styleUrl: './map.css',
})
export class Map implements OnInit, AfterViewInit {
  @Input() disasters: any[] = [];
  private map: any;

  ngOnInit(): void {}

  ngAfterViewInit(): void {
    this.initMap();
  }

  private initMap(): void {
    this.map = L.map('map').setView([20.5937, 78.9629], 5); // Center on India

    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      attribution: '© OpenStreetMap contributors'
    }).addTo(this.map);

    this.addDisasterMarkers();
  }

  private addDisasterMarkers(): void {
    this.disasters.forEach(disaster => {
      if (disaster.latitude && disaster.longitude) {
        const marker = L.marker([disaster.latitude, disaster.longitude])
          .addTo(this.map)
          .bindPopup(`
            <b>${disaster.title || disaster.disasterType}</b><br>
            Location: ${disaster.locationName}<br>
            Severity: ${disaster.severity}<br>
            Status: ${disaster.status}
          `);
      }
    });
  }
}
