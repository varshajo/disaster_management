import { Component, OnInit, AfterViewInit, Input, ElementRef, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Chart, registerables } from 'chart.js';

Chart.register(...registerables);

@Component({
  selector: 'app-chart',
  imports: [CommonModule],
  templateUrl: './chart.html',
  styleUrl: './chart.css',
})
export class ChartComponent implements OnInit, AfterViewInit {
  @Input() type: 'bar' | 'pie' | 'line' = 'bar';
  @Input() data: any = {};
  @Input() title: string = '';

  @ViewChild('canvas', { static: true }) canvas!: ElementRef<HTMLCanvasElement>;
  private chart: Chart | null = null;

  ngOnInit(): void {}

  ngAfterViewInit(): void {
    this.createChart();
  }

  ngOnChanges(): void {
    if (this.chart) {
      this.chart.update();
    }
  }

  private createChart(): void {
    const ctx = this.canvas.nativeElement.getContext('2d');
    if (ctx) {
      this.chart = new Chart(ctx, {
        type: this.type,
        data: this.data,
        options: {
          responsive: true,
          plugins: {
            title: {
              display: !!this.title,
              text: this.title
            }
          }
        }
      });
    }
  }
}
