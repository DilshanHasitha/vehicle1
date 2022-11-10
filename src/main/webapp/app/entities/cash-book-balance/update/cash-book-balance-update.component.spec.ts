import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { CashBookBalanceFormService } from './cash-book-balance-form.service';
import { CashBookBalanceService } from '../service/cash-book-balance.service';
import { ICashBookBalance } from '../cash-book-balance.model';
import { IMerchant } from 'app/entities/merchant/merchant.model';
import { MerchantService } from 'app/entities/merchant/service/merchant.service';
import { ITransactionType } from 'app/entities/transaction-type/transaction-type.model';
import { TransactionTypeService } from 'app/entities/transaction-type/service/transaction-type.service';

import { CashBookBalanceUpdateComponent } from './cash-book-balance-update.component';

describe('CashBookBalance Management Update Component', () => {
  let comp: CashBookBalanceUpdateComponent;
  let fixture: ComponentFixture<CashBookBalanceUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let cashBookBalanceFormService: CashBookBalanceFormService;
  let cashBookBalanceService: CashBookBalanceService;
  let merchantService: MerchantService;
  let transactionTypeService: TransactionTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [CashBookBalanceUpdateComponent],
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
      .overrideTemplate(CashBookBalanceUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CashBookBalanceUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    cashBookBalanceFormService = TestBed.inject(CashBookBalanceFormService);
    cashBookBalanceService = TestBed.inject(CashBookBalanceService);
    merchantService = TestBed.inject(MerchantService);
    transactionTypeService = TestBed.inject(TransactionTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Merchant query and add missing value', () => {
      const cashBookBalance: ICashBookBalance = { id: 456 };
      const merchant: IMerchant = { id: 50701 };
      cashBookBalance.merchant = merchant;

      const merchantCollection: IMerchant[] = [{ id: 51114 }];
      jest.spyOn(merchantService, 'query').mockReturnValue(of(new HttpResponse({ body: merchantCollection })));
      const additionalMerchants = [merchant];
      const expectedCollection: IMerchant[] = [...additionalMerchants, ...merchantCollection];
      jest.spyOn(merchantService, 'addMerchantToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ cashBookBalance });
      comp.ngOnInit();

      expect(merchantService.query).toHaveBeenCalled();
      expect(merchantService.addMerchantToCollectionIfMissing).toHaveBeenCalledWith(
        merchantCollection,
        ...additionalMerchants.map(expect.objectContaining)
      );
      expect(comp.merchantsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call TransactionType query and add missing value', () => {
      const cashBookBalance: ICashBookBalance = { id: 456 };
      const transactionType: ITransactionType = { id: 8930 };
      cashBookBalance.transactionType = transactionType;

      const transactionTypeCollection: ITransactionType[] = [{ id: 10984 }];
      jest.spyOn(transactionTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: transactionTypeCollection })));
      const additionalTransactionTypes = [transactionType];
      const expectedCollection: ITransactionType[] = [...additionalTransactionTypes, ...transactionTypeCollection];
      jest.spyOn(transactionTypeService, 'addTransactionTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ cashBookBalance });
      comp.ngOnInit();

      expect(transactionTypeService.query).toHaveBeenCalled();
      expect(transactionTypeService.addTransactionTypeToCollectionIfMissing).toHaveBeenCalledWith(
        transactionTypeCollection,
        ...additionalTransactionTypes.map(expect.objectContaining)
      );
      expect(comp.transactionTypesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const cashBookBalance: ICashBookBalance = { id: 456 };
      const merchant: IMerchant = { id: 59014 };
      cashBookBalance.merchant = merchant;
      const transactionType: ITransactionType = { id: 7685 };
      cashBookBalance.transactionType = transactionType;

      activatedRoute.data = of({ cashBookBalance });
      comp.ngOnInit();

      expect(comp.merchantsSharedCollection).toContain(merchant);
      expect(comp.transactionTypesSharedCollection).toContain(transactionType);
      expect(comp.cashBookBalance).toEqual(cashBookBalance);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICashBookBalance>>();
      const cashBookBalance = { id: 123 };
      jest.spyOn(cashBookBalanceFormService, 'getCashBookBalance').mockReturnValue(cashBookBalance);
      jest.spyOn(cashBookBalanceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cashBookBalance });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: cashBookBalance }));
      saveSubject.complete();

      // THEN
      expect(cashBookBalanceFormService.getCashBookBalance).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(cashBookBalanceService.update).toHaveBeenCalledWith(expect.objectContaining(cashBookBalance));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICashBookBalance>>();
      const cashBookBalance = { id: 123 };
      jest.spyOn(cashBookBalanceFormService, 'getCashBookBalance').mockReturnValue({ id: null });
      jest.spyOn(cashBookBalanceService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cashBookBalance: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: cashBookBalance }));
      saveSubject.complete();

      // THEN
      expect(cashBookBalanceFormService.getCashBookBalance).toHaveBeenCalled();
      expect(cashBookBalanceService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICashBookBalance>>();
      const cashBookBalance = { id: 123 };
      jest.spyOn(cashBookBalanceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cashBookBalance });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(cashBookBalanceService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
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
