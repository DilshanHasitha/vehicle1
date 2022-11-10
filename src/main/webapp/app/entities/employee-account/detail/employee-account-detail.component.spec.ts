import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { EmployeeAccountDetailComponent } from './employee-account-detail.component';

describe('EmployeeAccount Management Detail Component', () => {
  let comp: EmployeeAccountDetailComponent;
  let fixture: ComponentFixture<EmployeeAccountDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [EmployeeAccountDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ employeeAccount: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(EmployeeAccountDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(EmployeeAccountDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load employeeAccount on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.employeeAccount).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
