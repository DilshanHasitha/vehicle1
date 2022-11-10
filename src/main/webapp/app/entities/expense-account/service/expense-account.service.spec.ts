import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IExpenseAccount } from '../expense-account.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../expense-account.test-samples';

import { ExpenseAccountService, RestExpenseAccount } from './expense-account.service';

const requireRestSample: RestExpenseAccount = {
  ...sampleWithRequiredData,
  transactionDate: sampleWithRequiredData.transactionDate?.format(DATE_FORMAT),
};

describe('ExpenseAccount Service', () => {
  let service: ExpenseAccountService;
  let httpMock: HttpTestingController;
  let expectedResult: IExpenseAccount | IExpenseAccount[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ExpenseAccountService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a ExpenseAccount', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const expenseAccount = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(expenseAccount).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ExpenseAccount', () => {
      const expenseAccount = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(expenseAccount).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ExpenseAccount', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ExpenseAccount', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ExpenseAccount', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addExpenseAccountToCollectionIfMissing', () => {
      it('should add a ExpenseAccount to an empty array', () => {
        const expenseAccount: IExpenseAccount = sampleWithRequiredData;
        expectedResult = service.addExpenseAccountToCollectionIfMissing([], expenseAccount);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(expenseAccount);
      });

      it('should not add a ExpenseAccount to an array that contains it', () => {
        const expenseAccount: IExpenseAccount = sampleWithRequiredData;
        const expenseAccountCollection: IExpenseAccount[] = [
          {
            ...expenseAccount,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addExpenseAccountToCollectionIfMissing(expenseAccountCollection, expenseAccount);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ExpenseAccount to an array that doesn't contain it", () => {
        const expenseAccount: IExpenseAccount = sampleWithRequiredData;
        const expenseAccountCollection: IExpenseAccount[] = [sampleWithPartialData];
        expectedResult = service.addExpenseAccountToCollectionIfMissing(expenseAccountCollection, expenseAccount);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(expenseAccount);
      });

      it('should add only unique ExpenseAccount to an array', () => {
        const expenseAccountArray: IExpenseAccount[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const expenseAccountCollection: IExpenseAccount[] = [sampleWithRequiredData];
        expectedResult = service.addExpenseAccountToCollectionIfMissing(expenseAccountCollection, ...expenseAccountArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const expenseAccount: IExpenseAccount = sampleWithRequiredData;
        const expenseAccount2: IExpenseAccount = sampleWithPartialData;
        expectedResult = service.addExpenseAccountToCollectionIfMissing([], expenseAccount, expenseAccount2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(expenseAccount);
        expect(expectedResult).toContain(expenseAccount2);
      });

      it('should accept null and undefined values', () => {
        const expenseAccount: IExpenseAccount = sampleWithRequiredData;
        expectedResult = service.addExpenseAccountToCollectionIfMissing([], null, expenseAccount, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(expenseAccount);
      });

      it('should return initial array if no ExpenseAccount is added', () => {
        const expenseAccountCollection: IExpenseAccount[] = [sampleWithRequiredData];
        expectedResult = service.addExpenseAccountToCollectionIfMissing(expenseAccountCollection, undefined, null);
        expect(expectedResult).toEqual(expenseAccountCollection);
      });
    });

    describe('compareExpenseAccount', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareExpenseAccount(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareExpenseAccount(entity1, entity2);
        const compareResult2 = service.compareExpenseAccount(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareExpenseAccount(entity1, entity2);
        const compareResult2 = service.compareExpenseAccount(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareExpenseAccount(entity1, entity2);
        const compareResult2 = service.compareExpenseAccount(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
