import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { EmployeeTypeDetailComponent } from './employee-type-detail.component';

describe('EmployeeType Management Detail Component', () => {
  let comp: EmployeeTypeDetailComponent;
  let fixture: ComponentFixture<EmployeeTypeDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [EmployeeTypeDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ employeeType: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(EmployeeTypeDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(EmployeeTypeDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load employeeType on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.employeeType).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
