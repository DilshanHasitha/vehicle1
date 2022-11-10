import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { EmployeeAccountFormService } from './employee-account-form.service';
import { EmployeeAccountService } from '../service/employee-account.service';
import { IEmployeeAccount } from '../employee-account.model';
import { ITransactionType } from 'app/entities/transaction-type/transaction-type.model';
import { TransactionTypeService } from 'app/entities/transaction-type/service/transaction-type.service';
import { IMerchant } from 'app/entities/merchant/merchant.model';
import { MerchantService } from 'app/entities/merchant/service/merchant.service';
import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';

import { EmployeeAccountUpdateComponent } from './employee-account-update.component';

describe('EmployeeAccount Management Update Component', () => {
  let comp: EmployeeAccountUpdateComponent;
  let fixture: ComponentFixture<EmployeeAccountUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let employeeAccountFormService: EmployeeAccountFormService;
  let employeeAccountService: EmployeeAccountService;
  let transactionTypeService: TransactionTypeService;
  let merchantService: MerchantService;
  let employeeService: EmployeeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [EmployeeAccountUpdateComponent],
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
      .overrideTemplate(EmployeeAccountUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EmployeeAccountUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    employeeAccountFormService = TestBed.inject(EmployeeAccountFormService);
    employeeAccountService = TestBed.inject(EmployeeAccountService);
    transactionTypeService = TestBed.inject(TransactionTypeService);
    merchantService = TestBed.inject(MerchantService);
    employeeService = TestBed.inject(EmployeeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call TransactionType query and add missing value', () => {
      const employeeAccount: IEmployeeAccount = { id: 456 };
      const transactionType: ITransactionType = { id: 3209 };
      employeeAccount.transactionType = transactionType;

      const transactionTypeCollection: ITransactionType[] = [{ id: 69894 }];
      jest.spyOn(transactionTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: transactionTypeCollection })));
      const additionalTransactionTypes = [transactionType];
      const expectedCollection: ITransactionType[] = [...additionalTransactionTypes, ...transactionTypeCollection];
      jest.spyOn(transactionTypeService, 'addTransactionTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ employeeAccount });
      comp.ngOnInit();

      expect(transactionTypeService.query).toHaveBeenCalled();
      expect(transactionTypeService.addTransactionTypeToCollectionIfMissing).toHaveBeenCalledWith(
        transactionTypeCollection,
        ...additionalTransactionTypes.map(expect.objectContaining)
      );
      expect(comp.transactionTypesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Merchant query and add missing value', () => {
      const employeeAccount: IEmployeeAccount = { id: 456 };
      const merchant: IMerchant = { id: 47380 };
      employeeAccount.merchant = merchant;

      const merchantCollection: IMerchant[] = [{ id: 29917 }];
      jest.spyOn(merchantService, 'query').mockReturnValue(of(new HttpResponse({ body: merchantCollection })));
      const additionalMerchants = [merchant];
      const expectedCollection: IMerchant[] = [...additionalMerchants, ...merchantCollection];
      jest.spyOn(merchantService, 'addMerchantToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ employeeAccount });
      comp.ngOnInit();

      expect(merchantService.query).toHaveBeenCalled();
      expect(merchantService.addMerchantToCollectionIfMissing).toHaveBeenCalledWith(
        merchantCollection,
        ...additionalMerchants.map(expect.objectContaining)
      );
      expect(comp.merchantsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Employee query and add missing value', () => {
      const employeeAccount: IEmployeeAccount = { id: 456 };
      const employee: IEmployee = { id: 94124 };
      employeeAccount.employee = employee;

      const employeeCollection: IEmployee[] = [{ id: 8250 }];
      jest.spyOn(employeeService, 'query').mockReturnValue(of(new HttpResponse({ body: employeeCollection })));
      const additionalEmployees = [employee];
      const expectedCollection: IEmployee[] = [...additionalEmployees, ...employeeCollection];
      jest.spyOn(employeeService, 'addEmployeeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ employeeAccount });
      comp.ngOnInit();

      expect(employeeService.query).toHaveBeenCalled();
      expect(employeeService.addEmployeeToCollectionIfMissing).toHaveBeenCalledWith(
        employeeCollection,
        ...additionalEmployees.map(expect.objectContaining)
      );
      expect(comp.employeesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const employeeAccount: IEmployeeAccount = { id: 456 };
      const transactionType: ITransactionType = { id: 92724 };
      employeeAccount.transactionType = transactionType;
      const merchant: IMerchant = { id: 4442 };
      employeeAccount.merchant = merchant;
      const employee: IEmployee = { id: 98893 };
      employeeAccount.employee = employee;

      activatedRoute.data = of({ employeeAccount });
      comp.ngOnInit();

      expect(comp.transactionTypesSharedCollection).toContain(transactionType);
      expect(comp.merchantsSharedCollection).toContain(merchant);
      expect(comp.employeesSharedCollection).toContain(employee);
      expect(comp.employeeAccount).toEqual(employeeAccount);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEmployeeAccount>>();
      const employeeAccount = { id: 123 };
      jest.spyOn(employeeAccountFormService, 'getEmployeeAccount').mockReturnValue(employeeAccount);
      jest.spyOn(employeeAccountService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ employeeAccount });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: employeeAccount }));
      saveSubject.complete();

      // THEN
      expect(employeeAccountFormService.getEmployeeAccount).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(employeeAccountService.update).toHaveBeenCalledWith(expect.objectContaining(employeeAccount));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEmployeeAccount>>();
      const employeeAccount = { id: 123 };
      jest.spyOn(employeeAccountFormService, 'getEmployeeAccount').mockReturnValue({ id: null });
      jest.spyOn(employeeAccountService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ employeeAccount: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: employeeAccount }));
      saveSubject.complete();

      // THEN
      expect(employeeAccountFormService.getEmployeeAccount).toHaveBeenCalled();
      expect(employeeAccountService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEmployeeAccount>>();
      const employeeAccount = { id: 123 };
      jest.spyOn(employeeAccountService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ employeeAccount });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(employeeAccountService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareTransactionType', () => {
      it('Should forward to transactionTypeService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(transactionTypeService, 'compareTransactionType');
        comp.compareTransactionType(entity, entity2);
        expect(transactionTypeService.compareTransactionType).toHaveBeenCalledWith(entity, entity2);
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

    describe('compareEmployee', () => {
      it('Should forward to employeeService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(employeeService, 'compareEmployee');
        comp.compareEmployee(entity, entity2);
        expect(employeeService.compareEmployee).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
