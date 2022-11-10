import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { EmployeeAccountBalanceDetailComponent } from './employee-account-balance-detail.component';

describe('EmployeeAccountBalance Management Detail Component', () => {
  let comp: EmployeeAccountBalanceDetailComponent;
  let fixture: ComponentFixture<EmployeeAccountBalanceDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [EmployeeAccountBalanceDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ employeeAccountBalance: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(EmployeeAccountBalanceDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(EmployeeAccountBalanceDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load employeeAccountBalance on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.employeeAccountBalance).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
