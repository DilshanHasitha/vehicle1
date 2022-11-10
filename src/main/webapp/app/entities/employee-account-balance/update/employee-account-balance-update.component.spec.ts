import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { EmployeeAccountBalanceFormService } from './employee-account-balance-form.service';
import { EmployeeAccountBalanceService } from '../service/employee-account-balance.service';
import { IEmployeeAccountBalance } from '../employee-account-balance.model';
import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';
import { IMerchant } from 'app/entities/merchant/merchant.model';
import { MerchantService } from 'app/entities/merchant/service/merchant.service';
import { ITransactionType } from 'app/entities/transaction-type/transaction-type.model';
import { TransactionTypeService } from 'app/entities/transaction-type/service/transaction-type.service';

import { EmployeeAccountBalanceUpdateComponent } from './employee-account-balance-update.component';

describe('EmployeeAccountBalance Management Update Component', () => {
  let comp: EmployeeAccountBalanceUpdateComponent;
  let fixture: ComponentFixture<EmployeeAccountBalanceUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let employeeAccountBalanceFormService: EmployeeAccountBalanceFormService;
  let employeeAccountBalanceService: EmployeeAccountBalanceService;
  let employeeService: EmployeeService;
  let merchantService: MerchantService;
  let transactionTypeService: TransactionTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [EmployeeAccountBalanceUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(EmployeeAccountBalanceUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EmployeeAccountBalanceUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    employeeAccountBalanceFormService = TestBed.inject(EmployeeAccountBalanceFormService);
    employeeAccountBalanceService = TestBed.inject(EmployeeAccountBalanceService);
    employeeService = TestBed.inject(EmployeeService);
    merchantService = TestBed.inject(MerchantService);
    transactionTypeService = TestBed.inject(TransactionTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Employee query and add missing value', () => {
      const employeeAccountBalance: IEmployeeAccountBalance = { id: 456 };
      const employee: IEmployee = { id: 65847 };
      employeeAccountBalance.employee = employee;

      const employeeCollection: IEmployee[] = [{ id: 60895 }];
      jest.spyOn(employeeService, 'query').mockReturnValue(of(new HttpResponse({ body: employeeCollection })));
      const additionalEmployees = [employee];
      const expectedCollection: IEmployee[] = [...additionalEmployees, ...employeeCollection];
      jest.spyOn(employeeService, 'addEmployeeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ employeeAccountBalance });
      comp.ngOnInit();

      expect(employeeService.query).toHaveBeenCalled();
      expect(employeeService.addEmployeeToCollectionIfMissing).toHaveBeenCalledWith(
        employeeCollection,
        ...additionalEmployees.map(expect.objectContaining)
      );
      expect(comp.employeesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Merchant query and add missing value', () => {
      const employeeAccountBalance: IEmployeeAccountBalance = { id: 456 };
      const merchant: IMerchant = { id: 84929 };
      employeeAccountBalance.merchant = merchant;

      const merchantCollection: IMerchant[] = [{ id: 59383 }];
      jest.spyOn(merchantService, 'query').mockReturnValue(of(new HttpResponse({ body: merchantCollection })));
      const additionalMerchants = [merchant];
      const expectedCollection: IMerchant[] = [...additionalMerchants, ...merchantCollection];
      jest.spyOn(merchantService, 'addMerchantToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ employeeAccountBalance });
      comp.ngOnInit();

      expect(merchantService.query).toHaveBeenCalled();
      expect(merchantService.addMerchantToCollectionIfMissing).toHaveBeenCalledWith(
        merchantCollection,
        ...additionalMerchants.map(expect.objectContaining)
      );
      expect(comp.merchantsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call TransactionType query and add missing value', () => {
      const employeeAccountBalance: IEmployeeAccountBalance = { id: 456 };
      const transactionType: ITransactionType = { id: 3734 };
      employeeAccountBalance.transactionType = transactionType;

      const transactionTypeCollection: ITransactionType[] = [{ id: 19675 }];
      jest.spyOn(transactionTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: transactionTypeCollection })));
      const additionalTransactionTypes = [transactionType];
      const expectedCollection: ITransactionType[] = [...additionalTransactionTypes, ...transactionTypeCollection];
      jest.spyOn(transactionTypeService, 'addTransactionTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ employeeAccountBalance });
      comp.ngOnInit();

      expect(transactionTypeService.query).toHaveBeenCalled();
      expect(transactionTypeService.addTransactionTypeToCollectionIfMissing).toHaveBeenCalledWith(
        transactionTypeCollection,
        ...additionalTransactionTypes.map(expect.objectContaining)
      );
      expect(comp.transactionTypesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const employeeAccountBalance: IEmployeeAccountBalance = { id: 456 };
      const employee: IEmployee = { id: 59156 };
      employeeAccountBalance.employee = employee;
      const merchant: IMerchant = { id: 56200 };
      employeeAccountBalance.merchant = merchant;
      const transactionType: ITransactionType = { id: 95215 };
      employeeAccountBalance.transactionType = transactionType;

      activatedRoute.data = of({ employeeAccountBalance });
      comp.ngOnInit();

      expect(comp.employeesSharedCollection).toContain(employee);
      expect(comp.merchantsSharedCollection).toContain(merchant);
      expect(comp.transactionTypesSharedCollection).toContain(transactionType);
      expect(comp.employeeAccountBalance).toEqual(employeeAccountBalance);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEmployeeAccountBalance>>();
      const employeeAccountBalance = { id: 123 };
      jest.spyOn(employeeAccountBalanceFormService, 'getEmployeeAccountBalance').mockReturnValue(employeeAccountBalance);
      jest.spyOn(employeeAccountBalanceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ employeeAccountBalance });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: employeeAccountBalance }));
      saveSubject.complete();

      // THEN
      expect(employeeAccountBalanceFormService.getEmployeeAccountBalance).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(employeeAccountBalanceService.update).toHaveBeenCalledWith(expect.objectContaining(employeeAccountBalance));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEmployeeAccountBalance>>();
      const employeeAccountBalance = { id: 123 };
      jest.spyOn(employeeAccountBalanceFormService, 'getEmployeeAccountBalance').mockReturnValue({ id: null });
      jest.spyOn(employeeAccountBalanceService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ employeeAccountBalance: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: employeeAccountBalance }));
      saveSubject.complete();

      // THEN
      expect(employeeAccountBalanceFormService.getEmployeeAccountBalance).toHaveBeenCalled();
      expect(employeeAccountBalanceService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEmployeeAccountBalance>>();
      const employeeAccountBalance = { id: 123 };
      jest.spyOn(employeeAccountBalanceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ employeeAccountBalance });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(employeeAccountBalanceService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareEmployee', () => {
      it('Should forward to employeeService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(employeeService, 'compareEmployee');
        comp.compareEmployee(entity, entity2);
        expect(employeeService.compareEmployee).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareMerchant', () => {
      it('Should forward to merchantService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(merchantService, 'compareMerchant');
        comp.compareMerchant(entity, entity2);
        expect(merchantService.compareMerchant).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareTransactionType', () => {
      it('Should forward to transactionTypeService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(transactionTypeService, 'compareTransactionType');
        comp.compareTransactionType(entity, entity2);
        expect(transactionTypeService.compareTransactionType).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
