import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ResponderDashboard } from './responder-dashboard';

describe('ResponderDashboard', () => {
  let component: ResponderDashboard;
  let fixture: ComponentFixture<ResponderDashboard>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ResponderDashboard]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ResponderDashboard);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
